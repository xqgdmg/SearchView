package com.example.qhsj.searchview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView listView;
    private SearchView searchView;

    // 自动完成的列表
    private final String[] mStrings = { "aaaaa", "aaaaa1",
            "bbbbbb", "bbbbbb1",  "cccccc", "ddddddd" };
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        deleteUnderline();
        initListener();
    }

    /*
     * 右边的确定图片还会有下划线
     */
    private void deleteUnderline() {
        if (searchView != null) {
            try {        //--拿到字节码
                Class<?> argClass = searchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 // 点击 listView 的条目，进行搜索
                searchView.setQuery(mStrings[position],true);
            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        searchView = (SearchView) findViewById(R.id.searchView);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mStrings);
        listView.setAdapter(arrayAdapter);
        listView.setTextFilterEnabled(true);//设置lv可以被过虑

        // 设置该SearchView默认是否自动缩小为图标
        searchView.setIconifiedByDefault(false);
        // 为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(this);
        // 设置该SearchView显示搜索按钮（不知道怎么去除按钮的下划线，按钮也不好看，不需要）
        searchView.setSubmitButtonEnabled(false);
        // 设置该SearchView内默认显示的提示文本
        searchView.setQueryHint("查找");
    }

    /*
     * 单击搜索按钮时激发该方法
     * 默认 return false;
     */
    @Override
    public boolean onQueryTextSubmit(String queryText) {
        // 实际应用中应该在该方法内执行实际查询
        // 此处仅使用Toast显示用户输入的查询内容
        Toast.makeText(this, "您的选择是:" + queryText, Toast.LENGTH_LONG).show();
        return false;
    }

    /*
     * 用户输入字符时激发该方法
     * 默认 return false; 改为 true；应该和触摸事件的一样  return false 以后都不会执行，表示不需要处理，或者说没有得到处理，以后就不接收了
     */
/*    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("haha","onQueryTextChange==" + newText);

        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            listView.clearTextFilter();
        } else {
            // 根据用户输入的内容对ListView的列表项进行过滤（用这个会出现恶心的黑框吐司）
//            listView.setFilterText(newText);

             // listView.setFilterText(newText); 改成这段代码，去掉恶心的黑框吐司（但是就没有选择的效果了）
//            arrayAdapter.getFilter().filter(newText);


        }
        return true;
    }*/

    /*
     * 浮动框，这是listView.setFilterText(filterText)弹出来的。如果不想要这个浮动框，可以先获取Filter，然后调用Filter.filter(filterText)。
     * 使用这种方法不需要开启ListView的过滤功能。
     */
    @Override
    public boolean onQueryTextChange(String newText) {

        Log.e("haha","onQueryTextChange==" + newText);
        ListAdapter adapter = listView.getAdapter();

        if (adapter instanceof Filterable) {
            Filter filter = ((Filterable) adapter).getFilter();
            if (newText == null || newText.length() == 0) {
                filter.filter(null);
            } else {
                filter.filter(newText);
            }
        }
        return true;
    }
}
