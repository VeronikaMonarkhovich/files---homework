package com.taory;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.ZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {

    @Test
    void txtFileTest() throws Exception {
        String result;
        try (InputStream is = new FileInputStream("src/test/resources/Файл.txt")) {
            result = new String(is.readAllBytes());
            assertThat(result).contains("Привет!");
        }
    }

    @Test
    void pdfFileTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("Файл.pdf")) {
            PDF parsed = new PDF(stream);
            assertThat(parsed.text).contains("если ранее имели другие фамилию, имя, отчество, укажите их, когда меняли и где");
        }
    }

    @Test
    void xlsxFileTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("Файл.xlsx")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(5).getCell(0).getStringCellValue())
                    .isEqualTo("Работа");
        }
    }

    @Test
    public void zipFileTest() throws Exception {
        String pass = "123";
        String sourcePath = "./src/test/resources/Файл.zip";
        ZipFile zipFile = new ZipFile(sourcePath);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(pass.toCharArray());
            assertThat(zipFile.getFileHeaders().get(0).toString()).contains("14256.txt");
        }
    }

    @Test
    void docFileTest() throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("Файл.docx");
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(stream);
        String text = wordMLPackage.getMainDocumentPart().getContent().toString();
        assertThat(text).contains("Банковские реквизиты и юридический адрес");
    }
}