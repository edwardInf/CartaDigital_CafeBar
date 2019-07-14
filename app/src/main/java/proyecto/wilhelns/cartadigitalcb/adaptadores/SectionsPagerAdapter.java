package proyecto.wilhelns.cartadigitalcb.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import proyecto.wilhelns.cartadigitalcb.fragmentos.CartaFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CartaFragment.PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 5;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PISCO";
            case 1:
                return "RON";
            case 2:
                return "VODKA";
            case 3:
                return "WHISKY";
            case 4:
                return "PIQUEOS";
        }
        return null;
    }

}
