package com.torontodjango.reminder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Date;

public class TasksFragment extends Fragment {

    private ListView taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        taskList = (ListView)view.findViewById(R.id.task_list);
        TaskListAdapter taskListAdapter = listener.getTaskListAdapter();
        taskList.setAdapter(taskListAdapter);
        //taskList.setOnItemClickListener(listOnItemClickListener); // making it editable
        registerForContextMenu(taskList); // choose edit or delete

        return view;
    }

    interface OnFragmentInteractionListener {
        TaskListAdapter getTaskListAdapter();
        void onFragmentInteraction(int position);
    }

    OnFragmentInteractionListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Log.d("!!!!!", context.getClass().getName());
        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " should implement interface OnFragmentInteractionListener");
        }
    }

    private AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            listener.onFragmentInteraction(position);
        }
    };
}
