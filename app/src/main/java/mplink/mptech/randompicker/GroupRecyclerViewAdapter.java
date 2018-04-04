package mplink.mptech.randompicker;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mplink.mptech.randompicker.models.GroupModel;

/**
 * Created by Monkey Park on 3/6/2018.
 */

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder>{

    private List<GroupModel> groupList;
    private Context mCtx;

    public OnGroupClickListener mCallback;

    public interface OnGroupClickListener
    {
        void groupClick(GroupModel group);
        void groupEditClick(GroupModel group,List<GroupModel> list);
        void groupDelClick(GroupModel group);
    }


    public GroupRecyclerViewAdapter(List<GroupModel> groupList, OnGroupClickListener listener, Context context)
    {
        this.groupList = groupList;
        mCallback = listener;
        this.mCtx = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GroupModel group = groupList.get(position);
        if(group != null)
        {
            holder.txtGroupName.setText(group.getGroupName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.groupClick(group);
                }
            });


            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mCtx, v,Gravity.CENTER_HORIZONTAL);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.option_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mBtnEdit:
                                    mCallback.groupEditClick(group,groupList);
                                    break;
                                case R.id.mBtnDel:
                                    mCallback.groupDelClick(group);
                                    break;
                            }
                            return true;
                        }
                    });
                    //displaying the popup
                    popup.show();
                    return true;
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView txtGroupName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtGroupName = (TextView) itemView.findViewById(R.id.txtName);

            view = itemView;
        }
    }

}
