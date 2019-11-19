package com.example.words2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {
    private static boolean isTwoPane;
    private static MyDatebase dbHelper;
    static View view;
    static MainActivity mainActivity;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainActivity=(MainActivity) getActivity();
        dbHelper=new MyDatebase(mainActivity,"text_db",null,2);

        view = inflater.inflate(R.layout.news_title_frag, container, false);
        RecyclerView newsTitleRecyclerView = (RecyclerView)view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);
        return view;
    }



    public static List<News> getNews() {
        List<News> wordsList=new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor=db.query("word",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                News words=new News();
                String id=cursor.getString(cursor.getColumnIndex("id"));
                words.setTitle(id);
                String exl=cursor.getString(cursor.getColumnIndex("exl"));
                words.setContent(exl);
                String exp=cursor.getString(cursor.getColumnIndex("exp"));
                words.setContent(exp);
                wordsList.add(words);
            }while (cursor.moveToNext());
        }cursor.close();
        return wordsList;
    }

    private String getRandomLengthContent(String content){
        Random random = new Random();
        int length = random.nextInt(20)+1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < length;i++){
            builder.append(content);
        }
        return builder.toString();
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_layout) != null){
            isTwoPane = true;
        } else {
            isTwoPane = false;
        }
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<News> mNewsList;

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView newsTitleText;

            public ViewHolder(View view){
                super(view);
                newsTitleText = (TextView)view.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter(List<News> newsList){
            mNewsList = newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = mNewsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        NewsContentFragment newsContentFragment = (NewsContentFragment)getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    }else{
                        NewsContentActivity.actionStart(mainActivity, news.getTitle(), news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}
