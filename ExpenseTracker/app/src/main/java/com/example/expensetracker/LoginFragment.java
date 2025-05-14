package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    MyRepository myRepository;
    TextView name,username;
    EditText password;
    Button button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        myRepository=new MyRepository(requireActivity().getApplication());
        name=view.findViewById(R.id.loginname);
        username=view.findViewById(R.id.loginusername);
        password=view.findViewById(R.id.loginpass);
        button=view.findViewById(R.id.loginbutton);

        ArrayList<Category> categoryArrayList=myRepository.getAllCategories();
        if(categoryArrayList.size()==0){
            myRepository.addCategory(new Category("Food and Drinks"));
            myRepository.addCategory(new Category("Travel"));
            myRepository.addCategory(new Category("Utilities"));
        }

        User user=myRepository.getAllUsers().get(0);
        name.setText(user.getFirst_name());
        username.setText(user.getUsername());
        button.setOnClickListener(v -> {
            if(user.getPin()!=Integer.parseInt(password.getText().toString())) {
                Toast.makeText(getContext(),"Incorrect Password. Try Again",Toast.LENGTH_LONG).show();
                password.setText("");
            }
            else if(password.getText().toString().isEmpty()){
                Toast.makeText(getContext(),"Enter Password",Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });


        return view;
    }
}