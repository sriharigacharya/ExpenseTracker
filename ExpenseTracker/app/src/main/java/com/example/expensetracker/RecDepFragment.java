package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecDepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecDepFragment extends Fragment {

    private FloatingActionButton addnewrecdep;
    private MyRepository myRepository;
    private RecyclerView recyclerView;
    private List<RecurringExpense> recurringExpenseList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecDepFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecDepFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecDepFragment newInstance(String param1, String param2) {
        RecDepFragment fragment = new RecDepFragment();
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
        View view=inflater.inflate(R.layout.fragment_rec_dep, container, false);
        myRepository=new MyRepository(getActivity().getApplication());
        recurringExpenseList=myRepository.getAllRecurringExpenses();
        recyclerView=view.findViewById(R.id.recdeplist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecDeplistAdapter(getContext(),recurringExpenseList));

        addnewrecdep=view.findViewById(R.id.addrecdepbutton);
        addnewrecdep.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(),NewRecDepActivity.class);
            startActivity(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position=viewHolder.getAdapterPosition();
                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setTitle("Delete Recurring Expense")
                        .setMessage("Are you sure you want to delete this expense?")
                        .setPositiveButton("Delete",((dialog, which) -> {
                            RecurringExpense recurringExpense=recurringExpenseList.get(position);
                            myRepository.delRecurringExpense(recurringExpense);
                            recurringExpenseList.remove(recurringExpense);
                            recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                        }))
                        .setNegativeButton("Cancel",((dialog, which) -> {
                            recyclerView.getAdapter().notifyItemChanged(position);
                        }))
                        .setCancelable(false)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }
}