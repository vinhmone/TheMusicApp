package com.chaubacho.themusicapp.ui.main

import android.content.ComponentName
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.chaubacho.themusicapp.R
import com.chaubacho.themusicapp.base.BaseActivity
import com.chaubacho.themusicapp.broadcast.SongBroadcast
import com.chaubacho.themusicapp.data.model.Song
import com.chaubacho.themusicapp.databinding.ActivityMainBinding
import com.chaubacho.themusicapp.service.SongPlayerService
import com.chaubacho.themusicapp.ui.adapter.SongAdapter
import com.chaubacho.themusicapp.ui.notification.SongNotification.Companion.NEXT
import com.chaubacho.themusicapp.ui.notification.SongNotification.Companion.PLAY_PAUSE
import com.chaubacho.themusicapp.ui.notification.SongNotification.Companion.PREVIOUS
import com.chaubacho.themusicapp.ui.notification.SongNotificationCallback
import com.chaubacho.themusicapp.utils.RepositoryUtil
import com.chaubacho.themusicapp.utils.TimeConvert

class MainActivity : BaseActivity<ActivityMainBinding>(),
    SongContract.View,
    SeekBar.OnSeekBarChangeListener,
    View.OnClickListener,
    SongNotificationCallback {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
    private val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val songAdapter = SongAdapter(this::onSongClick)
    private val handler = Handler()
    private val songs = mutableListOf<Song>()
    private val savedSongs = mutableListOf<Song>()
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as SongPlayerService.SongBinder
            songPlayerService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            songPlayerService?.stopSelf()
        }
    }

    private var songPresenter: SongPresenter? = null
    private var playingSongId = -1
    private var songPlayerService: SongPlayerService? = null
    private var isShuffle = false
    private var isLoop = false
    private var songBroadcast: SongBroadcast? = null

    override fun initComponents() {
        if (checkPermission()) {
            initView()
            initData()
            initAdapter()
            initService()
        } else {
            askForPermission()
        }
    }

    override fun showAllSongs(songs: List<Song>) {
        this.songs.addAll(songs)
        songAdapter.updateData(this.songs)
    }

    override fun showError(e: String) {
        Toast.makeText(this, getString(R.string.error_get_data), Toast.LENGTH_LONG).show()
    }

    override fun onClick(v: View?) = with(viewBinding) {
        when (v) {
            imagePlayPauseSong -> playOrPauseSong()
            imageNextSong -> playNextSong()
            imagePreviousSong -> playPreviousSong()
            imageShuffleSongs -> shuffleSongs()
            imageLoopSong -> loopThisSong()
            else -> Unit
        }
    }

    override fun onNotificationPlayPauseSong() = playOrPauseSong()

    override fun onNotificationNextSong() = playNextSong()

    override fun onNotificationPreviousSong() = playPreviousSong()

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.progress?.let {
            changeToPlaying(true)
            songPlayerService?.seekTo(it)
        }
    }

    private fun initView() {
        viewBinding.run {
            seekbarControlTime.setOnSeekBarChangeListener(this@MainActivity)
            listOf(
                imagePlayPauseSong,
                imagePreviousSong,
                imageNextSong,
                imageShuffleSongs,
                imageLoopSong
            ).forEach {
                it.setOnClickListener(this@MainActivity)
            }
        }
    }

    private fun initAdapter() {
        viewBinding.recyclerSongs.adapter = songAdapter
    }

    private fun initData() {
        val repository = RepositoryUtil.getSongRepository(contentResolver)
        songPresenter = SongPresenter(this, repository)
        songPresenter?.start()
    }

    private fun initService() {
        songPlayerService = SongPlayerService()
        bindService(
            SongPlayerService.getIntent(this),
            serviceConnection,
            BIND_AUTO_CREATE
        )
        songBroadcast = SongBroadcast(this)
        IntentFilter().apply {
            addAction(NEXT)
            addAction(PLAY_PAUSE)
            addAction(PREVIOUS)
        }.run {
            registerReceiver(songBroadcast, this)
        }
    }

    private fun onSongClick(song: Song) {
        playingSongId = songs.indexOf(song)
        playThisSong(song)
    }

    private fun playThisSong(song: Song) {
        songPlayerService?.createMedia(song)
        songPlayerService?.play()
        showSongInfo()
    }

    private fun showSongInfo() {
        val currentSong = songs[playingSongId]
        viewBinding.textSongPlayingName.text = currentSong.name
        changeToPlaying(true)
        songPlayerService?.getDuration()?.let {
            viewBinding.textSongMaxTime.text = TimeConvert.convertMillisecondsToMinute(it)
            viewBinding.seekbarControlTime.max = it
        }
    }

    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                songPlayerService?.getCurrentPosition()?.let {
                    setSeekBar(it)
                    if (songPlayerService?.isPlaying() == true) {
                        handler.postDelayed(this, DELAY_TIME)
                    }
                }
            }
        }, DELAY_TIME)
    }

    private fun setSeekBar(time: Int) {
        viewBinding.seekbarControlTime.progress = time
        viewBinding.textSongCurrentTime.text = TimeConvert.convertMillisecondsToMinute(time)
        songPlayerService?.let {
            if (time >= it.getDuration()) {
                if (isLoop) playThisSong(songs[playingSongId])
                else playNextSong()
            }
        }
    }

    private fun playOrPauseSong() {
        if (songPlayerService?.isPlaying() == true) {
            changeToPlaying(false)
        } else {
            changeToPlaying(true)
        }
    }

    private fun playNextSong() {
        if (playingSongId >= songs.size - 1) playingSongId = -1
        playingSongId++
        playThisSong(songs[playingSongId])
    }

    private fun playPreviousSong() {
        if (playingSongId <= 0) {
            playingSongId = songs.size - 1
        } else {
            --playingSongId
        }
        playThisSong(songs[playingSongId])
    }

    private fun shuffleSongs() {
        if (isShuffle) {
            isShuffle = false
            songs.apply {
                clear()
                addAll(savedSongs)
                songAdapter.updateData(this)
            }
            viewBinding.imageShuffleSongs.setImageResource(R.drawable.ic_shuffle_off_24)
        } else {
            isShuffle = true
            savedSongs.apply {
                clear()
                addAll(songs)
            }
            songs.apply {
                shuffle()
                songAdapter.updateData(this)
            }
            viewBinding.imageShuffleSongs.setImageResource(R.drawable.ic_shuffle_on_24)
        }
    }

    private fun loopThisSong() {
        if (isLoop) {
            isLoop = false
            viewBinding.imageLoopSong.setImageResource(R.drawable.ic_loop_off_24)
        } else {
            isLoop = true
            viewBinding.imageLoopSong.setImageResource(R.drawable.ic_loop_on_24)
        }
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            for (p in permissions) {
                if (checkSelfPermission(p) == PackageManager.PERMISSION_DENIED)
                    return false
            }
        return true
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(permissions, 0)
    }

    private fun changeToPlaying(change: Boolean) {
        if (change) {
            songPlayerService?.play()
            viewBinding.imagePlayPauseSong.setImageResource(R.drawable.ic_pause_circle_filled_24)
            updateSeekBar()
        } else {
            songPlayerService?.pause()
            viewBinding.imagePlayPauseSong.setImageResource(R.drawable.ic_play_circle_filled_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        songPlayerService?.stopSelf()
        unregisterReceiver(songBroadcast)
    }

    companion object {
        const val DELAY_TIME = 500L
    }
}
