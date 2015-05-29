package com.example.administrador.myapplication.controllers.material;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.util.AppUtil;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.protocol.HTTP;

import java.util.List;

public class ServiceOrderListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    private RecyclerView mOrders;
    private ServiceOrderListAdapter mOrdersAdapter;
    private boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_list_material);

        bindElements();
    }

    private void bindElements() {
        mOrders = AppUtil.get(findViewById(R.id.recyclerViewServiceOrders));
        mOrders.setHasFixedSize(true);
        mOrders.setLayoutManager(new LinearLayoutManager(this));

        final FloatingActionButton fabAdd = AppUtil.get(findViewById(R.id.fabAdd));
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent goToAddActivity = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                startActivityForResult(goToAddActivity, REQUEST_CODE_ADD);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActive(true);
        updateAdapterItens(isActive());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final ServiceOrder selectItem = mOrdersAdapter.getLongClickItem();
        switch (item.getItemId()) {
            case R.id.actionCall:
                // Best Practices: http://stackoverflow.com/questions/4275678/how-to-make-phone-call-using-intent-in-android
                final Intent goToSOPhoneCall = new Intent(Intent.ACTION_CALL);
                goToSOPhoneCall.setData(Uri.parse("tel:" + selectItem.getPhone()));
                startActivity(goToSOPhoneCall);
                return true;
            case R.id.actionEdit:
                final Intent goToEdit = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                goToEdit.putExtra(ServiceOrderActivity.EXTRA_SERVICE_ORDER, selectItem);
                goToEdit.putExtra("EXTRA_BM", SystemClock.elapsedRealtime());
                goToEdit.putExtra(ServiceOrderActivity.EXTRA_START_BENCHMARK, REQUEST_CODE_EDIT);
                startActivity(goToEdit);
                return true;
            case R.id.actionActive:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.lbl_confirm)
                        .setMessage(R.string.msg_active)
                        .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectItem.setmActive(true);
                                selectItem.update();
                                Toast.makeText(ServiceOrderListActivity.this, R.string.msg_active_success, Toast.LENGTH_LONG).show();
                                updateAdapterItens(false);
                            }
                        })
                        .setNeutralButton(R.string.lbl_no, null)
                        .create().show();
                        return true;
            case R.id.actionInative:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.lbl_confirm_inative)
                        .setMessage(R.string.msg_inative)
                        .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectItem.setmActive(false);
                                selectItem.update();
                                Toast.makeText(ServiceOrderListActivity.this, R.string.msg_inative_success, Toast.LENGTH_LONG).show();
                                updateAdapterItens(true);
                            }
                        })
                        .setNeutralButton(R.string.lbl_no, null)
                        .create().show();
                        return true;
        }
        return false;
    }

    private void updateAdapterItens(boolean isActive) {
        final List<ServiceOrder> serviceOrders = ServiceOrder.getAll(isActive);
        if (mOrdersAdapter == null) {
            mOrdersAdapter = new ServiceOrderListAdapter(serviceOrders);
            mOrders.setAdapter(mOrdersAdapter);
        } else {
            mOrdersAdapter.setItens(serviceOrders);
            mOrdersAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_service_order_list_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @see <a href="http://developer.android.com/guide/components/intents-filters.html">Forcing an app chooser</a>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionShare:
                // Create the text message with a string
                final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ServiceOrder.getAll(true).toString());
                sendIntent.setType(HTTP.PLAIN_TEXT_TYPE);

                // Create intent to show the chooser dialog
                final Intent chooser = Intent.createChooser(sendIntent, "KamPow?");

                // Verify the original intent will resolve to at least one activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                return true;

            case R.id.actionActiveInative:
                if(isActive()){
                    item.setIcon(R.mipmap.ic_action_inative);
                    item.setTitle(R.string.lbl_inative);
                    setActive(false);
                }else{
                    item.setIcon(R.mipmap.ic_action_active);
                    item.setTitle(R.string.lbl_active);
                    setActive(true);
                }
                updateAdapterItens(isActive());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }
}
