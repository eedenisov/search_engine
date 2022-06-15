import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> allWords;

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        allWords = new HashMap<>();
        Map<String, Integer> wordAndCount = new HashMap<>();

        List<File> listOfPDFFiles = List.of(Objects.requireNonNull(pdfsDir.listFiles()));
        for (File filePdf : listOfPDFFiles) {
            var doc = new PdfDocument(new PdfReader(filePdf));

            for (var numberPage = 1; numberPage <= doc.getNumberOfPages(); numberPage++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(numberPage));
                var words = text.split("\\P{IsAlphabetic}+");

                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    wordAndCount.put(word.toLowerCase(), wordAndCount.getOrDefault(
                            word.toLowerCase(), 0) + 1);
                }
                int count;
                for (var w : wordAndCount.keySet()) {
                    String wordToLowerCase = w.toLowerCase();
                    if (wordAndCount.get(wordToLowerCase) != null) {
                        count = wordAndCount.get(wordToLowerCase);
                        allWords.computeIfAbsent(wordToLowerCase, k -> new ArrayList<>()).add(
                                new PageEntry(filePdf.getName(), numberPage, count));
                    }
                }
                wordAndCount.clear();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> resultSearch = new ArrayList<>();
        String wordToLowerCase = word.toLowerCase();
        if (allWords.get(wordToLowerCase) != null) {
            for (PageEntry pageEntry : allWords.get(wordToLowerCase)) {
                resultSearch.add(pageEntry);
            }
        }
        Collections.sort(resultSearch);
        return resultSearch;
    }

    @Override
    public String toString() {
        return "BooleanSearchEngine{" +
                "allWords=" + allWords +
                '}';
    }
}
