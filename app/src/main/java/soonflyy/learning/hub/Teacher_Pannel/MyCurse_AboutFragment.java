package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_Course_provide_Adapter;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.adapter.TestCourseOfferAdapter;
import soonflyy.learning.hub.model.T_Course_provide_Model;
import soonflyy.learning.hub.model.TestCourseofferModel;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCurse_AboutFragment extends Fragment implements VolleyResponseListener {
    RecyclerView recv_course;
TextView tv_courseoffer;
SwipeRefreshLayout swipe;
ImageView iv_dropdwn;
LinearLayout lin_offer;
    T_Course_provide_Adapter course_provide_adapter;
    ArrayList<T_Course_provide_Model> provide_modelList;
    public MyCurse_AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_curse__about, container, false);
        init_view(view);
        sendRequest(ApiCode.GET_COURSE_BY_ID);
        init_swipe_method();
        return  view;
    }
    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                init_course_recycler();
                init_courseoffer();
                swipe.setRefreshing(false);
            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange,R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }
    private void init_view(View view) {
        recv_course=view.findViewById(R.id.recv_course);
        swipe=view.findViewById(R.id.swipe);
        lin_offer =view.findViewById(R.id.lin_offer);
        iv_dropdwn=view.findViewById(R.id.iv_dropdwn);
    }

    private void init_course_recycler() {
        provide_modelList = new ArrayList<>();
        provide_modelList.clear ();
        recv_course.setHasFixedSize(true);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
       // linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recv_course.setLayoutManager(new GridLayoutManager(getActivity(), 3));

       // recv_course.setLayoutManager(linearLayoutManager);
        recv_course.setKeepScreenOn(true);
        provide_modelList.add(new T_Course_provide_Model( ));

        provide_modelList.add(new T_Course_provide_Model( ));
        provide_modelList.add(new T_Course_provide_Model( ));
        provide_modelList.add(new T_Course_provide_Model());

        course_provide_adapter = new T_Course_provide_Adapter(getContext(), provide_modelList);
        recv_course.setAdapter(course_provide_adapter);
        course_provide_adapter.notifyDataSetChanged();



    }

    private void init_courseoffer() {
        iv_dropdwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "dailoge open", Toast.LENGTH_SHORT).show();
                show_offer_dailoge();
            }
        });

    }

    private void show_offer_dailoge() {


        Dialog dialog = new Dialog (getActivity());
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.dailoge_create_courseoffer);
        dialog.getWindow ();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
        dialog.show ( );


        LinearLayout liner = dialog.findViewById(R.id.liner);
        LinearLayout lin_offer = dialog.findViewById(R.id.lin_offer);
        ImageView iv_dropup = dialog.findViewById(R.id.iv_dropup);
        SearchView search = dialog.findViewById(R.id.search);
        RecyclerView rec_offerlist = dialog.findViewById(R.id.rec_offerlist);
        TextView tv_courseoffer = dialog.findViewById(R.id.tv_courseoffer);
        TestCourseOfferAdapter offerAdapter;
        ArrayList<TestCourseofferModel> offerlist;

        dialog.show();
        iv_dropup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        offerlist = new ArrayList<>();
        offerlist.clear ();
        rec_offerlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_offerlist.setLayoutManager(linearLayoutManager);
        //rec_offerlist.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        recv_course.setKeepScreenOn(true);
        offerlist.add(new TestCourseofferModel( ));

        offerlist.add(new TestCourseofferModel( ));
        offerlist.add(new TestCourseofferModel( ));
        offerlist.add(new TestCourseofferModel());

        //offerAdapter = new TestCourseOfferAdapter(getContext(), offerlist);
        //rec_offerlist.setAdapter(offerAdapter);
        //offerAdapter.notifyDataSetChanged();




        dialog.setCanceledOnTouchOutside (false);
    }




    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {

            case ApiCode.GET_COURSE_BY_ID:
                params.put("course_id", "33");
                params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_COURSE_BY_ID, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_COURSE_BY_ID:
                service.postDataVolley(ApiCode.GET_COURSE_BY_ID,
                        BaseUrl.URL_GET_COURSE_BY_ID, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        switch (requestType) {
            case ApiCode.GET_COURSE_BY_ID:
                Log.e("course_details",response);
                JSONObject jsonObject=new JSONObject(response);
                if(jsonObject.getBoolean("status")){
//                    JSONArray jsonArray=jsonObject.getJSONArray("data");
//                    if(jsonArray.length()>0){
//                        model = (Course_DetailsMOdel) VolleyService.response(jsonArray.getJSONObject(0).toString(), Course_DetailsMOdel.class);
//                        setData();
//                    }
                }


                break;


        }
    }



}