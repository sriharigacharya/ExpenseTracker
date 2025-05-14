package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryPage extends AppCompatActivity {

    MyRepository myRepository;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    List<Category> categoryList;
    CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_category_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myRepository=new MyRepository(getApplication());
        categoryList=myRepository.getAllCategories();
        floatingActionButton=findViewById(R.id.addnewcategorybutton);
        recyclerView=findViewById(R.id.categorylistrecyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new CategoryListAdapter(getApplicationContext(),categoryList);
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NewCategoryActivity.class);
                startActivity(intent);
            }
        });
    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getAdapterPosition();
//            new AlertDialog.Builder(ManageCategoryPage.this)
//                    .setTitle("Delete Expense")
//                    .setMessage("Are you sure you want to delete this Category?")
//                    .setPositiveButton("Delete", (dialog, which) -> {
//                        Category category = categoryList.get(position);
//                        myRepository.delCategory(category);
//                        categoryList.remove(position);
//                        adapter.notifyItemRemoved(position);
//                    })
//                    .setNegativeButton("Cancel", (dialog, which) -> {
//                        adapter.notifyItemChanged(position);
//                    })
//                    .setCancelable(false)
//                    .show();
//        }
@Override
public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    int position = viewHolder.getAdapterPosition();
    Category categoryToDelete = categoryList.get(position);

    new AlertDialog.Builder(ManageCategoryPage.this)
            .setTitle("Category Deletion")
            .setMessage("Deleting this category will delete all expenses under it. Choose an action:")
            .setPositiveButton("Delete All", (dialog, which) -> {
                myRepository.delExpenseandCategory(categoryToDelete);
                categoryList.remove(position);
                adapter.notifyItemRemoved(position);
            })
            .setNeutralButton("Rename", (dialog, which) -> {
                showRenameDialog(categoryToDelete, position);
            })
            .setNegativeButton("Move Expenses", (dialog, which) -> {
                showMoveExpensesDialog(categoryToDelete, position);
            })
            .setCancelable(false)
            .show();
}


    }).attachToRecyclerView(recyclerView);
    }

    public void onResume(){
        super.onResume();
        categoryList.clear();
        categoryList.addAll(myRepository.getAllCategories());
        adapter.notifyDataSetChanged();
    }

    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }

    private void showRenameDialog(Category category, int position) {
        EditText input = new EditText(this);
        input.setText(category.getCategoryName());

        new AlertDialog.Builder(this)
                .setTitle("Rename Category")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        category.setCategoryName(newName);
                        myRepository.updatecategory(category);
                        adapter.notifyItemChanged(position);
                    } else {
                        adapter.notifyItemChanged(position); // Reset swipe if empty
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    adapter.notifyItemChanged(position);
                })
                .show();
    }

    private void showMoveExpensesDialog(Category categoryToDelete, int position) {
        List<Category> otherCategories = new ArrayList<>();
        for (Category cat : categoryList) {
            if (cat.getId()!=(categoryToDelete.getId())) {
                otherCategories.add(cat);
            }
        }

        if (otherCategories.isEmpty()) {
            Toast.makeText(this, "No other categories to move expenses into.", Toast.LENGTH_SHORT).show();
            adapter.notifyItemChanged(position);
            return;
        }

        String[] categoryNames = new String[otherCategories.size()];
        for (int i = 0; i < otherCategories.size(); i++) {
            categoryNames[i] = otherCategories.get(i).getCategoryName();
        }

        new AlertDialog.Builder(this)
                .setTitle("Move Expenses To")
                .setItems(categoryNames, (dialog, which) -> {
                    Category targetCategory = otherCategories.get(which);

                    myRepository.moveexpensetonewcat(categoryToDelete, targetCategory);
                    myRepository.delCategory(categoryToDelete);
                    categoryList.remove(position);
                    adapter.notifyItemRemoved(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    adapter.notifyItemChanged(position);
                })
                .show();
    }


}