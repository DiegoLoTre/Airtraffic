package airport.srl.org.airtraffic;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import airport.srl.org.airtraffic.airlines.*;
import airport.srl.org.airtraffic.airplane.*;
import airport.srl.org.airtraffic.airport.*;
import airport.srl.org.airtraffic.flights.*;

public class Airtraffic extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtraffic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager= getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.showAirport) {
            getSupportActionBar().setTitle("Airports");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, ShowAirports.newInstance())
                    .commit();
        }else if (id == R.id.airportData) {
            getSupportActionBar().setTitle("Search Airport");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskAirport.newInstance("Airport"))
                    .commit();
        }else if (id == R.id.departuresAirport) {
            getSupportActionBar().setTitle("Search Departures");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskAirport.newInstance("Departures"))
                    .commit();
        }else if (id == R.id.arrivalsAirport) {
            getSupportActionBar().setTitle("Search Arrivals");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskAirport.newInstance("Arrivals"))
                    .commit();
        }else if (id == R.id.addAirport) {
            getSupportActionBar().setTitle("Add New Airport");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AddAirport.newInstance())
                    .commit();
        }
        /*Airlines*/
        else if (id == R.id.showAirlines) {
            getSupportActionBar().setTitle("Airlines");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, ShowAirlines.newInstance())
                    .commit();
        }else if (id == R.id.airlinesData) {
            getSupportActionBar().setTitle("Search Airline");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskAirline.newInstance("Airlines"))
                    .commit();
        }
        else if (id == R.id.airlinesFlights) {
            getSupportActionBar().setTitle("Search Flights Per Airline");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskAirline.newInstance("AirlinesFlights"))
                    .commit();
        }
        else if (id == R.id.addAirline) {
            getSupportActionBar().setTitle("Add Airline");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AddAirline.newInstance())
                    .commit();
        }
        /*Airplanes*/
        else if (id == R.id.showAirplanes) {
            getSupportActionBar().setTitle("Airplane");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, ShowAirplane.newInstance())
                    .commit();
        }else if (id == R.id.airplaneData) {
            getSupportActionBar().setTitle("Search Airplane");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskAirplane.newInstance())
                    .commit();
        }else if (id == R.id.addAirplane) {
            getSupportActionBar().setTitle("Add Airplane");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AddAirplanes.newInstance())
                    .commit();
        }
        /*Flights*/
        else if (id == R.id.showFlights) {
            getSupportActionBar().setTitle("Flights");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, ShowFlights.newInstance())
                    .commit();
        }
        else if (id == R.id.searchFlights) {
            getSupportActionBar().setTitle("Search Flights Per Status");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskStatusFlight.newInstance("Status"))
                    .commit();
        }
        else if (id == R.id.checkFlight) {
            getSupportActionBar().setTitle("Search Flights Per Number");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AskFlight.newInstance())
                    .commit();
        }
        else if (id == R.id.addNewFlight) {
            getSupportActionBar().setTitle("Add Flights");
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, AddFlight.newInstance())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
