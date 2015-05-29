package com.example.administrador.myapplication.controllers.material;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.util.AppUtil;

import java.util.List;

public class ServiceOrderListAdapter extends RecyclerView.Adapter<ServiceOrderListAdapter.ViewHolder> {

    private List<ServiceOrder> mItens;

    /**
     * Context Menu
     */
    private int mPosition;

    public ServiceOrderListAdapter(List<ServiceOrder> itens) {
        mItens = itens;
    }

    public void setItens(List<ServiceOrder> itens) {
        this.mItens = itens;
    }

    /**
     * Context Menu
     */
    public ServiceOrder getLongClickItem() {
        return mItens.get(mPosition);
    }

    @Override
    public ServiceOrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.service_order_list_item_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = holder.mTxtDate.getContext();
        final ServiceOrder serviceOrder = mItens.get(position);
        holder.mTxtName.setText(serviceOrder.getClient());
        holder.mTxtDate.setText(AppUtil.formatDate(serviceOrder.getDate()));
        //holder.mTxtDate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.ic_text_date, 0, 0, 0);
        holder.mTxtValue.setText(AppUtil.formatDecimal(serviceOrder.getValue()));

        if (serviceOrder.isPaid()) {
            holder.mTxtValue.setTextColor(holder.mTxtName.getTextColors());
        } else {
            holder.mTxtValue.setTextColor(context.getResources().getColor(R.color.material_600));
        }

        /** Context Menu */
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPosition = holder.getLayoutPosition();
                return false;
            }
        });
        holder.mImageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context, view);
                // This context must implements OnMenuItemClickListener
                popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) context);
                popup.inflate(R.menu.menu_service_order_list_popup);
                MenuItem miActive = popup.getMenu().findItem(R.id.actionActive);
                miActive.setVisible(!serviceOrder.ismActive());

                MenuItem miInative = popup.getMenu().findItem(R.id.actionInative);
                miInative.setVisible(serviceOrder.ismActive());

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItens.size();
    }

    /**
     * Context Menu
     */
    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnClickListener(null);
        super.onViewRecycled(holder);
    }

    /**
     * Context Menu (View.OnCreateContextMenuListener)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTxtValue;
        public TextView mTxtName;
        public TextView mTxtDate;
        public ImageView mImageViewMenu;

        public ViewHolder(View view) {
            super(view);
            mTxtValue = AppUtil.get(view.findViewById(R.id.textViewValue));
            mTxtName = AppUtil.get(view.findViewById(R.id.textViewClient));
            mTxtDate = AppUtil.get(view.findViewById(R.id.textViewDate));
            mImageViewMenu = AppUtil.get(view.findViewById(R.id.imageViewMenu));
        }
    }
}
