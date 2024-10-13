package helper;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_app.R;
import com.example.recipe_app.nav.ViewPagerAdapterNav;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {

    // Thiết lập Navigation cho Activity với ViewPager và BottomNavigationView
    public static void setupViewPagerWithBottomNavigation(
            FragmentActivity activity,
            ViewPager viewPager,
            BottomNavigationView bottomNavigationView) {

        // Khởi tạo ViewPagerAdapter với FragmentManager và hành vi của adapter
        ViewPagerAdapterNav adapter = new ViewPagerAdapterNav(activity.getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter); // Đặt adapter cho ViewPager

        // Lắng nghe sự kiện thay đổi trang trên ViewPager để cập nhật trạng thái của BottomNavigationView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.homeIcon).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.categoryIcon).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.favouriteIcon).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.userIcon).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Lắng nghe sự kiện khi chọn item trong BottomNavigationView để thay đổi trang trên ViewPager
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeIcon) {
                    viewPager.setCurrentItem(0);
                } else if (itemId == R.id.categoryIcon) {
                    viewPager.setCurrentItem(1);
                } else if (itemId == R.id.favouriteIcon) {
                    viewPager.setCurrentItem(2);
                } else if (itemId == R.id.userIcon) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}
