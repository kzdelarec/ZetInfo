package hr.tvz.android.listazdelarec.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import hr.tvz.android.listazdelarec.R
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)

        toolbar.title = resources.getString(R.string.daily_lines)
        val fragment : Fragment = MainActivity()
        val args = Bundle()
        args.putString("status", "day")
        fragment.arguments = args
        supportFragmentManager.beginTransaction().add(R.id.replace, fragment).commit()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                toolbar.title = item.title
                val fragment : Fragment = MainActivity()
                val args = Bundle()
                args.putString("status", "day")
                fragment.arguments = args
                supportFragmentManager.beginTransaction().add(R.id.replace, fragment).addToBackStack("").commit()
            }
            R.id.nav_gallery -> {
                toolbar.title = item.title
                val fragment : Fragment = MainActivity()
                val args = Bundle()
                args.putString("status", "night")
                fragment.arguments = args
                supportFragmentManager.beginTransaction().replace(R.id.replace, fragment).addToBackStack("").commit()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
