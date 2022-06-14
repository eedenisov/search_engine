import java.util.Objects;

public class PageEntry implements Comparable<PageEntry> {
    private String pdfName;
    private int page;
    private int count;


    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public PageEntry() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PageEntry)) {
            return false;
        }
        try {
            PageEntry pageEntry = (PageEntry) obj;
            return pageEntry.getPdfName().equals(pdfName) &&
                    pageEntry.getCount() == count;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdfName, count);
    }

    @Override
    public int compareTo(PageEntry p) {
        if (count < p.getCount()) {
            return 1;
        } else if (count > p.getCount()) {
            return -1;
        }
        return pdfName.compareTo(p.pdfName);
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}';
    }
}
