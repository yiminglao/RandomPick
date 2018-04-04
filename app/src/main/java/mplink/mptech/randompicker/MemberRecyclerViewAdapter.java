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

import mplink.mptech.randompicker.models.MemberModel;

/**
 * Created by Monkey Park on 3/25/2018.
 */

public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder>{

    private List<MemberModel> memberList;

    private Context mCtx;

    public onMemberClickListener mCallback;

    public interface onMemberClickListener
    {
        void memberEditClick(MemberModel member);
        void memberDelClick(MemberModel member);

    }

    public MemberRecyclerViewAdapter(List<MemberModel> memberList, Context context, onMemberClickListener listener)
    {
        this.memberList = memberList;
        this.mCtx = context;
        mCallback = listener;

    }




    @Override
    public MemberRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MemberRecyclerViewAdapter.ViewHolder holder, int position) {
        final MemberModel member = memberList.get(position);

        if(member != null)
        {
            holder.txtMemberName.setText(member.getMemberName());

            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mCtx, v, Gravity.CENTER_HORIZONTAL);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.option_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mBtnEdit:
                                    mCallback.memberEditClick(member);
                                    break;
                                case R.id.mBtnDel:
                                    mCallback.memberDelClick(member);
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

        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View view;

        private TextView txtMemberName;

        public ViewHolder(View itemView) {
            super(itemView);

            txtMemberName = (TextView) itemView.findViewById(R.id.txtName);

            view = itemView;

        }
    }
}
