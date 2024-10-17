package com.example.fragmentlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class ItemListFragment extends Fragment {

    private ArrayList<String> itemList;
    private ItemAdapter itemAdapter;

    public ItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore the itemList from savedInstanceState or create a new one
        itemList = savedInstanceState != null ? savedInstanceState.getStringArrayList("items") : new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

        Button addButton = view.findViewById(R.id.button_add_item);
        addButton.setOnClickListener(v -> addItem());

        Button deleteButton = view.findViewById(R.id.button_delete_item);
        deleteButton.setOnClickListener(v -> deleteItem());

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            itemList.clear();
            itemAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Items cleared!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("items", itemList);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveItemList(itemList); // Save the item list on pause
    }

    private void saveItemList(ArrayList<String> items) {
        // Logic to save the items (to shared preferences, database, etc.)
        Toast.makeText(getContext(), "Item list saved!", Toast.LENGTH_SHORT).show();
    }

    private void addItem() {
        itemList.add("New Item " + (itemList.size() + 1));
        itemAdapter.notifyItemInserted(itemList.size() - 1);
    }

    private void deleteItem() {
        if (!itemList.isEmpty()) {
            // Remove the last item from the list
            String deletedItem = itemList.remove(itemList.size() - 1);
            itemAdapter.notifyItemRemoved(itemList.size()); // Notify adapter about item removal
            Toast.makeText(getContext(), "Deleted: " + deletedItem, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "No items to delete!", Toast.LENGTH_SHORT).show();
        }
    }
}
