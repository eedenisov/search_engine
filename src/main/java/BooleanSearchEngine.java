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

        List<File> listOfPdfFiles = List.of(Objects.requireNonNull(pdfsDir.listFiles()));
        for (File filePdf : listOfPdfFiles) {
            var doc = new PdfDocument(new PdfReader(filePdf));

            for (var i = 1; i < doc.getNumberOfPages(); i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                }
                int count;
                for (var word : freqs.keySet()) {
                    String w = word.toLowerCase();
                    if (freqs.get(w) != null) {
                        count = freqs.get(w);
                        allWords.computeIfAbsent(w, key -> new ArrayList<>()).add(
                                new PageEntry(filePdf.getName(), i, count));
                    }
                }
                freqs.clear();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> result = new ArrayList<>();

        if (word != null) {
            String w = word.toLowerCase();

            if (allWords.get(w) != null) {
                result.addAll(allWords.get(w));
            }
            Collections.sort(result);
        }
        return result;
    }


    @Override
    public String toString() {
        return "BooleanSearchEngine{" +
                "allWords=" + allWords +
                "}";
    }
}
