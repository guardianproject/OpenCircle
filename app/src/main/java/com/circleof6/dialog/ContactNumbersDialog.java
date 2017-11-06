package com.circleof6.dialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.circleof6.R;
import com.circleof6.dialog.utils.ConstantsDialog;

import java.util.ArrayList;

/**
 * Created by Edgar Salvador Maurilio on 23/10/2015.
 */
public class ContactNumbersDialog extends DialogFragment implements DialogInterface.OnClickListener
{
    private ArrayList<String>       phonesNumbers;
    private OnSelectContactListener onSelectContactListener;


    public static ContactNumbersDialog getInstance(ArrayList<String> phoneNumbers)
    {
        ContactNumbersDialog dialog = new ContactNumbersDialog();
        Bundle bundleArg = new Bundle();
        bundleArg.putStringArrayList(ConstantsDialog.ARG_PHONES, phoneNumbers);
        dialog.setArguments(bundleArg);
        return dialog;
    }

    public void setOnSelectContactListener(OnSelectContactListener onSelectContactListener)
    {
        this.onSelectContactListener = onSelectContactListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_number);
        ArrayAdapter<String> adapter = crateAdapter();
        builder.setAdapter(adapter, this);

        return builder.create();
    }

    @NonNull
    private ArrayAdapter<String> crateAdapter()
    {
        phonesNumbers = getArguments().getStringArrayList(ConstantsDialog.ARG_PHONES);
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, phonesNumbers);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int position)
    {
        onSelectContactListener.onSelectPhone(phonesNumbers.get(position));
    }

    public interface OnSelectContactListener
    {
        void onSelectPhone(String phone);
    }
}
