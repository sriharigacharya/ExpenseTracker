package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    EditText firstname,lastname,username,password,confirmpass;
    Button signupbutton;
    MyRepository myRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_signup, container, false);
        firstname=view.findViewById(R.id.signupfirstname);
        lastname=view.findViewById(R.id.signuplastname);
        username=view.findViewById(R.id.signupusername);
        password=view.findViewById(R.id.signuppassword);
        confirmpass=view.findViewById(R.id.signupconfirmpass);
        signupbutton=view.findViewById(R.id.signupsubmit);

        myRepository=new MyRepository(requireActivity().getApplication());

        signupbutton.setOnClickListener(v -> {

            if (firstname.getText().toString().trim().isEmpty() ||
                    lastname.getText().toString().trim().isEmpty() ||
                    username.getText().toString().trim().isEmpty() ||
                    password.getText().toString().trim().isEmpty() ||
                    confirmpass.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
            else if(password.getText().toString().trim().length()<6){
                Toast.makeText(getContext(), "Pin should be a 6 digit number", Toast.LENGTH_SHORT).show();
            }
            else if(!(password.getText().toString()).equals(confirmpass.getText().toString())){
                Toast.makeText(getContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                password.setText("");
                confirmpass.setText("");
            }
            else {
                myRepository.addUser(new User(username.getText().toString().trim(),
                        Integer.parseInt(password.getText().toString()),
                        firstname.getText().toString().trim(),
                        lastname.getText().toString().trim()));
                Intent intent=new Intent(getContext(),LoginSignupActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }


        });


        return view;
    }
}