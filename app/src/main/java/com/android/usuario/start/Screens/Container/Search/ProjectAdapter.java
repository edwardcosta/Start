package com.android.usuario.start.Screens.Container.Search;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.R;

import java.util.List;

/**
 * Created by tulio on 15/05/2017.
 */

public class ProjectAdapter extends ArrayAdapter<Project> {
    public ProjectAdapter(Context context, int resource, List<Project> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.project_card, parent, false);
        }

        TextView projectName = (TextView) convertView.findViewById(R.id.projectName);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView authorName = (TextView) convertView.findViewById(R.id.authorName);

        Project project = getItem(position);

        projectName.setText(project.getName());
        description.setText(project.getDescription());
        authorName.setText(project.getAuthor());

        return convertView;
    }
}
