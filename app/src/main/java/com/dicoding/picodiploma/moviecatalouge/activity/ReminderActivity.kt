package com.dicoding.picodiploma.moviecatalouge.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.adapter.MovieReleaseAdapter
import com.dicoding.picodiploma.moviecatalouge.reminder.ReminderReceiver
import com.dicoding.picodiploma.moviecatalouge.viewModel.MovieReleaseViewModel
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {
    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var movieReleaseAdapter: MovieReleaseAdapter
    private lateinit var movieReleaseViewModel: MovieReleaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = getString(R.string.reminder)
        actionbar?.elevation = 0f

        reminderReceiver = ReminderReceiver()
        movieReleaseAdapter = MovieReleaseAdapter()
        movieReleaseAdapter.notifyDataSetChanged()

        rvMoviesRelease.layoutManager =  GridLayoutManager(this, 1)
        rvMoviesRelease.adapter = movieReleaseAdapter

        movieReleaseViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieReleaseViewModel::class.java)
        movieReleaseViewModel.setMovieRelease()

        movieReleaseViewModel.getMovieRelease().observe(this, Observer { movieItems ->
            if (movieItems != null) {
                movieReleaseAdapter.setDataMovieRelease(movieItems)
            }
        })

        swDaily.isChecked = reminderReceiver.isReminderSet(this, ReminderReceiver.TYPE_DAILY)
        swRelease.isChecked = reminderReceiver.isReminderSet(this, ReminderReceiver.TYPE_RELEASE)

        swDaily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                reminderReceiver.setReminder(this, ReminderReceiver.TYPE_DAILY, "07:00", "Selamat pagi, nonton film yuk!")
                Toast.makeText(applicationContext, getString(R.string.dailyReminder) +" "+ getString(R.string.statusOn), Toast.LENGTH_SHORT).show()
            }else{
                reminderReceiver.cancelReminder(this, ReminderReceiver.TYPE_DAILY)
                Toast.makeText(applicationContext, getString(R.string.dailyReminder) +" "+ getString(R.string.statusOff), Toast.LENGTH_SHORT).show()
            }
        }

        swRelease.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                reminderReceiver.setReminder(this, ReminderReceiver.TYPE_RELEASE, "08:00", "Hei, cek ada rilis film baru nih!")
                Toast.makeText(applicationContext, getString(R.string.releaseReminder) +" "+ getString(R.string.statusOn), Toast.LENGTH_SHORT).show()
            }else{
                reminderReceiver.cancelReminder(this, ReminderReceiver.TYPE_RELEASE)
                Toast.makeText(applicationContext, getString(R.string.releaseReminder) +" "+ getString(R.string.statusOff), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val moveMainActivity = Intent(this, MainActivity::class.java)
            startActivity(moveMainActivity)
        }
        return super.onOptionsItemSelected(item)
    }
}
