package com.example.istreamapp.ui.home

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.istreamapp.R
import com.example.istreamapp.data.AppDatabase
import com.example.istreamapp.model.PlaylistItem
import com.example.istreamapp.ui.auth.LoginFragment
import com.example.istreamapp.ui.playlist.PlaylistFragment
import com.example.istreamapp.utils.SessionManager
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var isPlayerPageLoaded = false
    private var pendingVideoId: String? = null
    private var pendingUrlFromPlaylist: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUrl = view.findViewById<EditText>(R.id.etYoutubeUrl)
        val btnPlay = view.findViewById<Button>(R.id.btnPlay)
        val btnAdd = view.findViewById<Button>(R.id.btnAddToPlaylist)
        val btnMyPlaylist = view.findViewById<Button>(R.id.btnMyPlaylist)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val webView = view.findViewById<WebView>(R.id.webViewPlayer)

        val dao = AppDatabase.getDatabase(requireContext()).appDao()

        setupWebView(webView)
        loadIframePlayerPage(webView)

        pendingUrlFromPlaylist = arguments?.getString("video_url")
        pendingUrlFromPlaylist?.let { selectedUrl ->
            etUrl.setText(selectedUrl)
        }

        btnPlay.setOnClickListener {
            val url = etUrl.text.toString().trim()
            playFromUrl(url, webView)
        }

        btnAdd.setOnClickListener {
            val url = etUrl.text.toString().trim()
            val username = SessionManager.getLoggedInUser(requireContext())

            if (url.isEmpty()) {
                Toast.makeText(requireContext(), "Enter a URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username == null) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                dao.insertPlaylistItem(
                    PlaylistItem(
                        username = username,
                        videoUrl = url
                    )
                )

                Toast.makeText(requireContext(), "Added to playlist", Toast.LENGTH_SHORT).show()
                etUrl.text.clear()
            }
        }

        btnMyPlaylist.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlaylistFragment())
                .addToBackStack(null)
                .commit()
        }

        btnLogout.setOnClickListener {
            SessionManager.logout(requireContext())

            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        pendingUrlFromPlaylist?.let { selectedUrl ->
            playFromUrl(selectedUrl, webView)
        }
    }

    private fun playFromUrl(url: String, webView: WebView) {
        if (url.isEmpty()) {
            Toast.makeText(requireContext(), "Enter a URL", Toast.LENGTH_SHORT).show()
            return
        }

        val videoId = extractYoutubeVideoId(url)

        if (videoId == null) {
            Toast.makeText(requireContext(), "Invalid URL", Toast.LENGTH_SHORT).show()
        } else {
            playVideoInIframe(webView, videoId)
        }
    }

    private fun setupWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isPlayerPageLoaded = true

                pendingVideoId?.let {
                    playVideoInIframe(webView, it)
                    pendingVideoId = null
                }
            }
        }
    }

    private fun loadIframePlayerPage(webView: WebView) {
        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    html, body {
                        margin: 0;
                        padding: 0;
                        width: 100%;
                        height: 100%;
                        background: black;
                    }
                    #player {
                        width: 100%;
                        height: 100%;
                    }
                </style>
            </head>
            <body>
                <div id="player"></div>

                <script>
                    var tag = document.createElement('script');
                    tag.src = "https://www.youtube.com/iframe_api";
                    var firstScriptTag = document.getElementsByTagName('script')[0];
                    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

                    var player;
                    var pendingVideoId = null;

                    function onYouTubeIframeAPIReady() {
                        player = new YT.Player('player', {
                            width: '100%',
                            height: '100%',
                            playerVars: {
                                playsinline: 1,
                                rel: 0
                            },
                            events: {
                                'onReady': onPlayerReady
                            }
                        });
                    }

                    function onPlayerReady(event) {
                        if (pendingVideoId) {
                            player.loadVideoById(pendingVideoId);
                            pendingVideoId = null;
                        }
                    }

                    function loadVideoByApp(videoId) {
                        if (player && typeof player.loadVideoById === 'function') {
                            player.loadVideoById(videoId);
                        } else {
                            pendingVideoId = videoId;
                        }
                    }
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(
            "https://www.youtube.com",
            html,
            "text/html",
            "utf-8",
            null
        )
    }

    private fun playVideoInIframe(webView: WebView, videoId: String) {
        if (!isPlayerPageLoaded) {
            pendingVideoId = videoId
            return
        }

        webView.evaluateJavascript(
            "loadVideoByApp('$videoId');",
            null
        )
    }

    private fun extractYoutubeVideoId(url: String): String? {
        return when {
            url.contains("youtu.be/") -> {
                url.substringAfter("youtu.be/")
                    .substringBefore("?")
                    .substringBefore("&")
                    .takeIf { it.isNotBlank() }
            }

            url.contains("youtube.com/watch?v=") -> {
                url.substringAfter("v=")
                    .substringBefore("&")
                    .takeIf { it.isNotBlank() }
            }

            url.contains("youtube.com/embed/") -> {
                url.substringAfter("embed/")
                    .substringBefore("?")
                    .substringBefore("&")
                    .takeIf { it.isNotBlank() }
            }

            else -> null
        }
    }
}