package com.example.travelmaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ListView listView;
    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_blank, container, false);
         listView=view.findViewById(R.id.it2);
        firebaseDatabase = FirebaseDatabase.getInstance("");
        databaseReference=firebaseDatabase.getReference("placesinfo");
        List<Places> placesList=new ArrayList<>();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Map<String,String> map=(Map<String, String>) snapshot.getValue();
                Places places=new Places();
                places.setStreetad(map.get("streetad"));
                places.setState(map.get("state"));
                places.setCountry(map.get("country"));
                places.setImage(map.get("image"));
                placesList.add(places);

                MyAdapter myAdapter=new MyAdapter(getActivity(),placesList);
                listView.setAdapter(myAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }


        });




        return view;

    }
    public class MyAdapter extends BaseAdapter{
        Context context;
        List<Places> stringList;
        TextView t1;
        ImageView img;

        public MyAdapter(Context context,List<Places>stringList) {
            this.context = context;
            this.stringList=stringList;
        }

        @Override
        public int getCount() {
            return stringList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=LayoutInflater.from(context).inflate(R.layout.places_layout,viewGroup,false);
            t1=view.findViewById(R.id.t1);
            img=view.findViewById(R.id.i123);

            t1.setText("Place:"+stringList.get(i).getStreetad()+
            "\nState:"+stringList.get(i).getState()+
                            "\nCountry:"+stringList.get(i).getCountry()
            );


            byte[] bimage=Base64.decode(stringList.get(i).getImage().getBytes(),Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(bimage,0,bimage.length);
            img.setImageBitmap(bitmap);
            return view;
        }
    }
}