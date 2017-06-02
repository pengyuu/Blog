package info.shenlan.blog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import info.shenlan.blog.MainActivity;
import info.shenlan.blog.R;


/**
 * @author:JackTony
 * @tips :菜单的fragment
 * @date :2013-11-17
 */
public class MenuFragment extends Fragment {

    private MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_menu, null);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] items = {"第一个栏目", "第二个栏目"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, items);
        ListView menuLv = (ListView) getActivity().findViewById(R.id.menu_list);
        //设置适配器和监听器
        menuLv.setAdapter(adapter);
        menuLv.setOnItemClickListener(new MenuItemClickListener());
    }

    class MenuItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment newFragment = null;
            MainActivity.NOW_FRAGMENT_NO = position;
            activity.supportInvalidateOptionsMenu();

            switch (position) {
                case 0:
                    newFragment = new MainFragment();
                    break;
                case 1:
                    newFragment = new SecondFragment();
                    break;
            }

            if (newFragment != null) {
                //交给Activity来切换正文fragment
                activity.switchContent(newFragment);
            }
        }
    }

}
