package com.danielacedo.manageproductrecycler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.danielacedo.manageproductrecycler.adapter.ProductAdapter;
import com.danielacedo.manageproductrecycler.interfaces.IProduct;
import com.danielacedo.manageproductrecycler.model.Product;

/**
 * Activity inherating from ListActivity that displays all the products in out Applicationś List
 * @author Daniel Acedo Calderón
 */
public class Product_Activity extends AppCompatActivity {
    public final static int REQUEST_CODE_ADD_PRODUCT = 1; //Request code to start an Activity for adding products
    public final static int REQUEST_CODE_EDIT_PRODUCT = 2;

    private ProductAdapter adapter;
    private ListView lv_ListProduct;
    private FloatingActionButton fab_AddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_);

        //Adapter
        adapter = new ProductAdapter(this);
        lv_ListProduct = (ListView) findViewById(R.id.lv_listProduct);
        lv_ListProduct.setAdapter(adapter);
        lv_ListProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IProduct.PRODUCT_KEY, (Product)parent.getItemAtPosition(position));

                Intent intent = new Intent(Product_Activity.this, ManageProduct_Activity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT);
            }
        });

        //FloatingActionButton
        fab_AddProduct = (FloatingActionButton) findViewById(R.id.fab_AddProduct);
        fab_AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_Activity.this, ManageProduct_Activity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT);
            }
        });

    }

    /*
        Inflates menu in the activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product, menu); //Inflates the menu resource in the menu object
        return super.onCreateOptionsMenu(menu);
    }

    /*
        Code to be executed for every menuitem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()){
            case R.id.action_add_product:
                intent = new Intent(Product_Activity.this, ManageProduct_Activity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT);
                break;
            case R.id.action_sort_alphabetically:
                adapter.getAlphabeticallySortedProducts();
                break;
            case R.id.action_settings_general:
                intent = new Intent(Product_Activity.this, GeneralSettings_Activity.class);
                startActivity(intent);
                break;
            case R.id.action_settings_account:
                intent = new Intent(Product_Activity.this, AccountSettings_Activity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles callbacks for activities called with startActivityForResult
     * @param requestCode Request code used for starting the activity
     * @param resultCode Code sent from the started activity
     * @param data Data sent from the started activity
     * @author Daniel Acedo Calderón
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQUEST_CODE_ADD_PRODUCT:
                if(resultCode == RESULT_OK){
                    Product product = (Product) data.getSerializableExtra(IProduct.PRODUCT_KEY);
                    ((ProductAdapter)lv_ListProduct.getAdapter()).addProduct(product);
                }
                break;

            case REQUEST_CODE_EDIT_PRODUCT:
                if(resultCode == RESULT_OK){
                    Product product = (Product) data.getSerializableExtra(IProduct.PRODUCT_KEY);
                    ((ProductAdapter)lv_ListProduct.getAdapter()).editProduct(product);
                }
        }

    }
}
