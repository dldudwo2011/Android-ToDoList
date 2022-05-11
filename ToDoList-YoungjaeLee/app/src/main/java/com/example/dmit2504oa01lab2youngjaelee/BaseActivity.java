package com.example.dmit2504oa01lab2youngjaelee;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.DialogFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BaseActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Integer hexThemeLightBlue= Color.parseColor("#ADD8E6");
    Integer hexThemeGreen=Color.parseColor("#90EE90");
    Integer hexThemePink=Color.parseColor("#FFC0CB");

    // Activity code here

    public void showSetUsernameDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new SetUsernameDialogFragment();
        dialog.show(getSupportFragmentManager(), "SetUsernameDialogFragment");
    }

    public void showSetPasswordDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new SetPasswordDialogFragment();
        dialog.show(getSupportFragmentManager(), "SetPasswordFragment");
    }

    public void showThemeDialog() {
        SetThemeBottomSheetDialog bottomSheet = new SetThemeBottomSheetDialog();
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    public void showFontDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new FontDialogFragment();
        dialog.show(getSupportFragmentManager(), "FontDialogFragment");
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_main, popup.getMenu());

        try {
            Field field = popup.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popup);
            Class<?> cls = Class.forName("com.android.internal.view.menu.MenuPopupHelper");
            Method method = cls.getDeclaredMethod("setForceShowIcon", new Class[]{boolean.class});
            method.setAccessible(true);
            method.invoke(menuPopupHelper, new Object[]{true});
        } catch (Exception e) {
            e.printStackTrace();
        }


        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_username:
            {
                showSetUsernameDialog();
                return true;
            }
            case R.id.menu_item_password:
            {
                showSetPasswordDialog();
                return true;
            }
            case R.id.menu_item_font:
            {
                showFontDialog();
                return true;
            }
            case R.id.menu_item_theme:
            {
                showThemeDialog();
                return true;
            }
            default:
                return false;
        }
    }

}
