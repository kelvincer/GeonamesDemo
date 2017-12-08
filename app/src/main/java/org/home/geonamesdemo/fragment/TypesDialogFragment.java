package org.home.geonamesdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import org.home.geonamesdemo.R;
import org.home.geonamesdemo.listener.CloseDialogCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 22/11/2017.
 */

public class TypesDialogFragment extends DialogFragment {


    List<Integer> mSelectedItems;
    CloseDialogCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (CloseDialogCallback) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList();  // Where we track the selected items

        boolean[] checkedItems = new boolean[10];

        for (int i = 0; i < checkedItems.length; i++) {
            if (i == 0)
                checkedItems[i] = true;
            else
                checkedItems[i] = false;
        }

        mSelectedItems.add(0);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title

        builder.setTitle("Escoge los tipos de Lugares")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.clases, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {

                                if (isChecked) {
                                    if (which == 0) {
                                        for (Integer i : mSelectedItems)
                                            ((AlertDialog) dialog).getListView().setItemChecked(i, false);

                                        mSelectedItems.clear();
                                        mSelectedItems.add(0);
                                    } else {
                                        ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                                        mSelectedItems.remove(Integer.valueOf(0));
                                        mSelectedItems.add(which);
                                    }
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.getListView().setItemChecked(3, true);
        alertDialog.show();
        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        callback.onClose(mSelectedItems);
    }
}
