package com.example.googletranslatordemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.googletranslatordemo.databinding.ActivityMainBinding;
import com.example.googletranslatordemo.models.Input;
import com.example.googletranslatordemo.models.Language;
import com.example.googletranslatordemo.models.Result;
import com.example.googletranslatordemo.models.Translate;
import com.example.googletranslatordemo.models.TranslateResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setInput(new Input());
        binding.setLanguage(new Language());
        binding.setLifecycleOwner(this);
        binding.setViewModel(mainViewModel);

        mainViewModel.translateResponse.observe(this, new Observer<Result<TranslateResponse>>() {
            @Override
            public void onChanged(Result<TranslateResponse> result) {
                if (result != null) {
                    if (result instanceof Result.Success) {
                        TranslateResponse translateResponse = ((Result.Success<TranslateResponse>) result).data;
                        binding.outputEt.setText(translateResponse.getOutput());
                    } else {
                        String errorMessage = ((Result.Error<TranslateResponse>) result).exception.getMessage();
                        Log.d("TAG", "Error :" + errorMessage);
                    }
                }
            }
        });


        List<Translate> translateList = new ArrayList<>();
        //Read xls file from assets
        try {
            InputStream inputStream = getAssets().open("ENGLISH.xls");
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("UTF-8");
            ws.setGCDisabled(true);
            Workbook workbook = Workbook.getWorkbook(inputStream, ws);
            Sheet sheet = workbook.getSheet("ENGLISH");
            for (int i = 0; i < sheet.getRows(); i++) {
                Cell[] cells = sheet.getRow(i);
                if (cells[0].getContents().trim().isEmpty())
                    break;
                translateList.add(new Translate(
                        cells[0].getContents(),
                        cells[1].getContents(),
                        ""
                ));
                /*Log.d("TAG", cells[0].getContents() + " : "
                        + cells[1].getContents());*/
            }
        } catch (IOException | BiffException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        mainViewModel.translateAndSaveAll(translateList);
        //createFile(Uri.parse("/csvfile/"));
    }


    WritableWorkbook workbook;

    void createExcelSheet(Uri uri)  {
        try {

            File file=new File(uri.getPath());

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            workbook = Workbook.createWorkbook(file, wbSettings);
            createFirstSheet();
//            createSecondSheet();
            //closing cursor
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createFirstSheet() {
        try {
            List<Translate> listdata = new ArrayList<>();

            listdata.add(new Translate("a", "b", "c"));
            listdata.add(new Translate("a", "b", "c"));
            listdata.add(new Translate("a", "b", "c"));
            listdata.add(new Translate("a", "b", "c"));
            listdata.add(new Translate("a", "b", "c"));
            listdata.add(new Translate("a", "b", "c"));
            //Excel sheet name. 0 (number)represents first sheet
            WritableSheet sheet = workbook.createSheet("ENG_ENG", 0);
            // column and row title
            sheet.addCell(new Label(0, 0, "id"));
            sheet.addCell(new Label(1, 0, "english"));
            sheet.addCell(new Label(2, 0, "translate"));

            for (int i = 0; i < listdata.size(); i++) {
                sheet.addCell(new Label(0, i + 1, listdata.get(i).getId()));
                sheet.addCell(new Label(1, i + 1, listdata.get(i).getEnglishText()));
                sheet.addCell(new Label(2, i + 1, listdata.get(i).getTranslatedText()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int CREATE_FILE = 1;

    private void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.ms-excel");
        intent.putExtra(Intent.EXTRA_TITLE, "english");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker before your app creates the document.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for directory that
            // the user selected.
            if (data != null) {
                Log.d("TAG", data.getData().toString());
                //createExcelSheet(data.getData());
            }

        }
    }


}