package com.danielacedo.manageproductrecycler;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.danielacedo.manageproductrecycler.adapter.ProductAdapter;
import com.danielacedo.manageproductrecycler.interfaces.IProduct;
import com.danielacedo.manageproductrecycler.interfaces.ProductPresenter;
import com.danielacedo.manageproductrecycler.model.Product;
import com.danielacedo.manageproductrecycler.presenter.ProductPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity inherating from ListActivity that displays all the products in out Applicationś List
 * @author Daniel Acedo Calderón
 */
public class MultiListProductFragment extends Fragment implements ProductPresenter.View {

    private ProductAdapter adapter;
    private ListView lv_ListProduct;
    private TextView txv_empty;
    private FloatingActionButton fab_AddProduct;

    private List<View> hiddenViews;

    private MultiListProductListener mCallback;
    ProductPresenter presenter;

    interface MultiListProductListener{
        void showManageProduct(Bundle bundle);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (MultiListProductListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(getContext().toString() + "must implement MultiListProductListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ProductAdapter(getContext());
        presenter = new ProductPresenterImpl(this);

        hiddenViews = new ArrayList<>();

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (presenter != null) {
            presenter.loadProducts();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootview = inflater.inflate(R.layout.fragment_product, null);

        lv_ListProduct = (ListView) rootview.findViewById(R.id.lv_listProduct);
        txv_empty = (TextView)rootview.findViewById(android.R.id.empty);

        //FloatingActionButton
        fab_AddProduct = (FloatingActionButton) rootview.findViewById(R.id.fab_AddProduct);

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hiddenViews.add(fab_AddProduct);

        lv_ListProduct.setAdapter(adapter);
        lv_ListProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(IProduct.PRODUCT_KEY, (Product)parent.getItemAtPosition(position));

                mCallback.showManageProduct(bundle);
            }
        });


        lv_ListProduct.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        SimpleMultiChoiceModeListener mcl = new SimpleMultiChoiceModeListener(getActivity(), presenter, hiddenViews);
        lv_ListProduct.setMultiChoiceModeListener(mcl);
        lv_ListProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                lv_ListProduct.setItemChecked(position, !lv_ListProduct.isItemChecked(position));
                Log.d("Prueba", "Pulsado largo");
                return false;
            }
        });


        fab_AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showManageProduct(null);
            }
        });

    }

    /*
                    Inflates menu in the fragment
        */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_listproduct, menu); //Inflates the menu resource in the menu object
    }

    /*
            Code to be executed for every menuitem
         */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_product:
                mCallback.showManageProduct(null);
                break;
            case R.id.action_sort_alphabetically:
                adapter.getAlphabeticallySortedProducts();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProducts(List<Product> products){
        adapter.updateProduct(products);
    }

    private void hideList(boolean hide){
        lv_ListProduct.setVisibility(hide ? View.GONE : View.VISIBLE);
        txv_empty.setVisibility(hide ? View.VISIBLE : View.GONE);
    }

    public void showEmptyState(boolean show){
        hideList(show);
    }

    public void showMessage(String message){

    }

    @Override
    public void showMessageDelete(final Product product){
        Snackbar.make(getView(), "Producto eliminado", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        presenter.addProduct(product);
                        presenter.loadProducts();
                    }
                }).show();

        //SETCALLBACK: Hacer una llamada a un metodo callback de un snackbar
        //Incluso si el snackbar se ha eliminado mediante swipe
        /*.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);

                if(event != DISMISS_EVENT_ACTION){
                    presenter.deleteProduct(product);
                }
            }
        }).show();*/
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        presenter = null;
    }
}