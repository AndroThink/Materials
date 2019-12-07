package com.androthink.materials.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androthink.materials.R;
import com.androthink.materials.adapter.FileFolderAdapter;
import com.androthink.materials.callback.FileFolderCallback;
import com.androthink.materials.callback.RequestCallback;
import com.androthink.materials.callback.SelectPathCallback;
import com.androthink.materials.databinding.DialogSelectFileFolderBinding;
import com.androthink.materials.models.FileFolderModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressLint("ValidFragment")
public class SelectFileFileFolderUtils extends DialogFragment implements RequestCallback, FileFolderCallback {

    public static void selectDirectory(@NonNull AppCompatActivity activity, String title, SelectPathCallback callback) {
        FragmentManager fm = activity.getSupportFragmentManager();
        SelectFileFileFolderUtils dialog = new SelectFileFileFolderUtils(title, null, false, callback);
        dialog.show(fm, "SelectPath");
    }

    public static void selectFile(@NonNull AppCompatActivity activity, String title, SelectPathCallback callback) {
        FragmentManager fm = activity.getSupportFragmentManager();
        SelectFileFileFolderUtils dialog = new SelectFileFileFolderUtils(title, null, true, callback);
        dialog.show(fm, "SelectPath");
    }

    public static void selectDirectory(@NonNull AppCompatActivity activity, String title, String directory, SelectPathCallback callback) {
        FragmentManager fm = activity.getSupportFragmentManager();
        SelectFileFileFolderUtils dialog = new SelectFileFileFolderUtils(title, directory, false, callback);
        dialog.show(fm, "SelectPath");
    }

    public static void selectFile(@NonNull AppCompatActivity activity, String title, String directory, SelectPathCallback callback) {
        FragmentManager fm = activity.getSupportFragmentManager();
        SelectFileFileFolderUtils dialog = new SelectFileFileFolderUtils(title, directory, true, callback);
        dialog.show(fm, "SelectPath");
    }

    private String location;
    private boolean pickFiles;
    private final String title;
    private final String reqLocation;

    private final SelectPathCallback callback;

    private DialogSelectFileFolderBinding binding;

    //Folders and Files have separate lists because we show all folders first then files
    private ArrayList<FileFolderModel> folderAndFileList;
    private ArrayList<FileFolderModel> foldersList;
    private ArrayList<FileFolderModel> filesList;

    private FileFolderAdapter fileFolderAdapter;

    @SuppressLint("ValidFragment")
    private SelectFileFileFolderUtils(String title, String reqLocation, boolean pickFiles, SelectPathCallback callback) {
        this.title = title;
        this.pickFiles = pickFiles;
        this.reqLocation = reqLocation;
        this.callback = callback;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        }

        setCancelable(false);

        binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_file_folder, null, false);

        if (getActivity() != null) {
            PermissionsUtils permissionsUtils = PermissionsUtils.getInstance(((AppCompatActivity) getActivity()));

            if (permissionsUtils.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    permissionsUtils.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                prepare();
            } else {
                permissionsUtils.createPermissionRequest(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SelectFileFileFolderUtils.this);
            }
        } else {
            SelectFileFileFolderUtils.this.dismiss();
        }

        binding.tvTitle.setText(title);

        binding.rvFileFolders.setItemAnimator(new DefaultItemAnimator());
        binding.rvFileFolders.setLayoutManager(new LinearLayoutManager(getContext()));

        if (pickFiles) {
            binding.btnSelect.setVisibility(View.GONE);
            binding.btnCreateNewFolder.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        binding.btnBack.performClick();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void prepare() {

        if (getActivity() != null) {
            File file = getActivity().getExternalFilesDir(null);
            if (file != null)
                location = file.getAbsolutePath();
        }

        if (reqLocation != null) {
            File requestedFolder = new File(reqLocation);
            if (requestedFolder.exists())
                location = reqLocation;
        }

        setupListeners();

        loadLists(location);
    }

    @Override
    public void onCallback(String[] permissions, @NonNull boolean[] grantResults) {
        if (grantResults[0] && grantResults[1])
            prepare();
        else
            SelectFileFileFolderUtils.this.dismiss();
    }

    private void loadLists(@NonNull String location) {

        binding.btnBack.setEnabled(true);

        File folder = new File(location);

        if (!folder.exists() || !folder.isDirectory()) {
            callback.onError("Invalid Location !");
            SelectFileFileFolderUtils.this.dismiss();
            return;
        }

        binding.tvLocation.setText((getString(R.string.path) + " " + folder.getAbsolutePath()));
        File[] files = folder.listFiles();

        filesList = new ArrayList<>();
        foldersList = new ArrayList<>();

        if (files != null) {
            for (File currentFile : files) {
                if (currentFile.isDirectory())
                    foldersList.add(new FileFolderModel(currentFile.getName(), true));
                else
                    filesList.add(new FileFolderModel(currentFile.getName(), false));
            }
        }

        // sort & add to final List - as we show folders first add folders first to the final list
        Collections.sort(foldersList, comparatorAscending);
        folderAndFileList = new ArrayList<>();
        folderAndFileList.addAll(foldersList);

        //if we have to show files, then add files also to the final list
        if (pickFiles) {
            Collections.sort(filesList, comparatorAscending);
            folderAndFileList.addAll(filesList);
        }

        fileFolderAdapter = new FileFolderAdapter(folderAndFileList, SelectFileFileFolderUtils.this);
        binding.rvFileFolders.setAdapter(fileFolderAdapter);
    }

    private Comparator<FileFolderModel> comparatorAscending = new Comparator<FileFolderModel>() {
        @Override
        public int compare(@NonNull FileFolderModel f1, @NonNull FileFolderModel f2) {
            return f1.getName().compareTo(f2.getName());
        }
    };

    @Override
    public void onFileFolderClicked(int position) {
        if (pickFiles && !folderAndFileList.get(position).isFolder()) {
            callback.onSelected((location + File.separator + folderAndFileList.get(position).getName()));
            SelectFileFileFolderUtils.this.dismiss();
        } else {
            location = location + File.separator + folderAndFileList.get(position).getName();
            loadLists(location);
        }
    }

    private void setupListeners() {
        binding.btnCreateNewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                    dialog.setTitle(getString(R.string.enter_folder_name));

                    final EditText et = new EditText(getActivity());
                    dialog.setView(et);

                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.create),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    createNewFolder(et.getText().toString());
                                }
                            });

                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btn_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    dialog.show();
                }
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null && !location.equals("") && !location.equals("/")) {
                    int start = location.lastIndexOf('/');
                    String temp = location.substring(0, start);

                    if (temp.equals("/storage/emulated"))
                        binding.btnBack.setEnabled(false);
                    else {
                        location = temp;
                        loadLists(location);
                    }
                } else
                    binding.btnBack.setEnabled(false);
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectFileFileFolderUtils.this.dismiss();
            }
        });

        binding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickFiles) {
                    Toast.makeText(getActivity(), R.string.you_have_to_select_file, Toast.LENGTH_LONG).show();
                } else {
                    callback.onSelected(location);
                    SelectFileFileFolderUtils.this.dismiss();
                }
            }
        });
    }

    private void createNewFolder(String filename) {
        try {
            File file = new File(location + File.separator + filename);
            if (file.exists()) {
                Toast.makeText(getContext(), R.string.already_used_folder_name, Toast.LENGTH_SHORT).show();
                return;
            }
            if (file.mkdirs()) {
                location = file.getAbsolutePath();
                loadLists(location);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
