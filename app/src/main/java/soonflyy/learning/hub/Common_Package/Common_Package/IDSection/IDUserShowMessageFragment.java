package soonflyy.learning.hub.Common_Package.Common_Package.IDSection;

import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity.SCHOOL_ID_SECTION_USER_TYPE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.ShowMessageAdapter;
import soonflyy.learning.hub.Common_Package.Models.ShowMessageModel;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;


public class IDUserShowMessageFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    CardView card_toolbar;
    CircleImageView profile_image;
    SearchView searchView;
    ImageView iv_back;
    RecyclerView rec_msg;
    ShowMessageAdapter adapter;
    ArrayList<ShowMessageModel> modellist=new ArrayList<>();
    String user_type, profile_url;

    public IDUserShowMessageFragment() {
        // Required empty public constructor
    }


    public static IDUserShowMessageFragment newInstance(String param1, String param2) {
        IDUserShowMessageFragment fragment = new IDUserShowMessageFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_i_d_user_show_message, container, false);
        initViewid(view);
        getArgumentData();
        sendRequest(ApiCode.GET_CHAT_LIST_BY_ID);
        //init_Recyclerview();
        setSearch();
        return view;
    }

    private void setSearch() {
        CommonMethods.setSearchViewColor(getActivity(),searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter!=null){
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter!=null){
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    private void getArgumentData() {
        user_type = getArguments().getString("user_type");
        profile_url = getArguments().getString("profile_image");
        Picasso.get().load(profile_url).placeholder(R.drawable.profile).into(profile_image);
    }

    private void init_Recyclerview() {
        rec_msg.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_msg.setLayoutManager(layoutManager);

//        modellist = new ArrayList<>();
//        modellist.add(new ShowMessageModel());
//        modellist.add(new ShowMessageModel());
//        modellist.add(new ShowMessageModel());

        adapter = new ShowMessageAdapter(getActivity(), modellist, new ShowMessageAdapter.OnChatListItemClickListener() {
            @Override
            public void onItemClick(int position, ShowMessageModel model) {

                goForChat(model);
            }
        });
        rec_msg.setAdapter(adapter);

    }

    private void goForChat(ShowMessageModel model) {
        IDUserMessageFragment fragment=new IDUserMessageFragment();
        Bundle bundle=new Bundle();
        bundle.putString("profile_image",profile_url);
        bundle.putString("user_name",model.getName());
        bundle.putString("to_id",model.getTo_id());
        bundle.putString("user_type",user_type);
        bundle.putString("toType",model.getType());

        bundle.putString("from_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));//USER_ID
        fragment.setArguments(bundle);
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
        }else if (user_type.equals("teacher")) {
            ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        }else{
            ((MainActivity) getActivity()).SwitchFragment(fragment);
        }
    }

    private void initViewid(View view) {
        searchView=view.findViewById(R.id.et_search);
        rec_msg = view.findViewById(R.id.rec_msg);
        iv_back = view.findViewById(R.id.iv_back);
        card_toolbar = view.findViewById(R.id.card_toolbar);
        profile_image = view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.profile_image:
                gotoManageProfile("edit",new SessionManagement(getContext()).getString(ID_SECTION_USER_ID));
                break;
        }

    }

    private void goBack() {
        try {
            if (user_type.equals(SCHOOL_COACHING)){
                ((SchoolMainActivity)getActivity()).onBackPressed();
            }else if (user_type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).onBackPressed();
            } else {
                ((MainActivity) getActivity()).onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void gotoManageProfile(String type,String user_id) {
        IDUserViewProfileFragment fragment=new IDUserViewProfileFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        bundle.putString("user_type",user_type);
        bundle.putString("user_id",user_id);
        fragment.setArguments(bundle);
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
        }else if (user_type.equals("teacher"))
            ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        else
            ((MainActivity) getActivity()).SwitchFragment(fragment);
    }


    private void sendRequest(int request) {
        HashMap<String, Object> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Id");
        switch (request) {
            case ApiCode.GET_CHAT_LIST_BY_ID:
               // params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("user_id", new SessionManagement(getContext()).getString(ID_SECTION_USER_ID));
               setTypeParameter(params);
               reference.child(new SessionManagement(getContext()).getString(ID_SECTION_USER_ID)).child("Message").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       modellist.clear();
                       if (snapshot.getChildrenCount() > 0) {

                           for(DataSnapshot ds:snapshot.getChildren()){
                               ShowMessageModel showMessageModel=new ShowMessageModel();
                               String toId="";
                               long timestamp=0;
                               Log.e("TOOOOOOOOJHVJHV",""+ds.getChildrenCount());
                              for(DataSnapshot ds1:ds.getChildren()){
                                  toId=ds1.child("to_id").getValue(String.class);

                                  timestamp++;


                                  if(timestamp==ds.getChildrenCount()){
                                      showMessageModel.setLast_msg(ds1.child("message").getValue(String.class));
                                      showMessageModel.setFrom_id(ds1.child("from_id").getValue(String.class));

                                      showMessageModel.setTo_id(ds1.child("to_id").getValue(String.class));
                                      showMessageModel.setFrom_type(ds1.child("from_type").getValue(String.class));
                                      showMessageModel.setTo_type(ds1.child("to_type").getValue(String.class));

                                  }else{


                                  }

                              }
//                              Log.e("TOOOOOOOOJHVJHV",toId);
//                              Log.e("TOOOOOOOOJHVJHV",showMessageModel.getLast_msg());
                              DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Id");
                              reference1.child(toId).addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                      showMessageModel.setPhoto(snapshot.child("photo").getValue(String.class));
                                      showMessageModel.setName(snapshot.child("name").getValue(String.class));
                                      Log.e("GHCFTDfthcycytj",showMessageModel.getTo_id());
                                      modellist.add(showMessageModel);
                                      init_Recyclerview();

                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError error) {

                                  }
                              });
                           }






                       } else {

                           init_Recyclerview();
                           CommonMethods.showSuccessToast(getContext(),"No Users");
                       }


                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

//                callApi(ApiCode.GET_CHAT_LIST_BY_ID, params);
                break;
        }
    }
    private void setTypeParameter(HashMap<String, Object> params) {
        if (user_type.equals(SCHOOL_COACHING)){
            if (SCHOOL_ID_SECTION_USER_TYPE.equals(SCHOOL_COACHING)){
                params.put("type","0");
            }else{
                params.put("type","1");
            }

        }else {
            params.put("type","1");
        }
    }


    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_CHAT_LIST_BY_ID:
                service.postDataVolley(ApiCode.GET_CHAT_LIST_BY_ID,
                        BaseUrl.URL_GET_CHAT_LIST_BY_ID, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
//
            case ApiCode.GET_CHAT_LIST_BY_ID:
                Log.e("chatlist ", response);
                if (jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("users");

//                    if (jsonArray.length() > 0) {
//                        ArrayList<ShowMessageModel> psearch = new Gson().
//                                fromJson(jsonArray.toString(),
//                                        new TypeToken<List<ShowMessageModel>>() {
//                                        }.getType());
//                        modellist.clear();
//                        modellist.addAll(psearch);
//                        init_Recyclerview();
//                    } else {
//                        modellist.clear();
//                        init_Recyclerview();
//                        CommonMethods.showSuccessToast(getContext(),"No Users");
//                    }
                }

                break;
        }
    }
}