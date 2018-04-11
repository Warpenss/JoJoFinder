package Main.Tools;

public class PageCounter {
    private static final int BUTTONS_TO_SHOW = 5;
    private int firstPage;
    private int lastPage;

    public PageCounter(int totalPages, int currentPage) {

        int halfBoxSize = BUTTONS_TO_SHOW / 2;

        if (totalPages <= BUTTONS_TO_SHOW) {
            setFirstPage(1);
            setLastPage(totalPages);
        } else if (currentPage - halfBoxSize <= 0) {
            setFirstPage(1);
            setLastPage(BUTTONS_TO_SHOW);
        } else if (currentPage + halfBoxSize == totalPages) {
            setFirstPage(currentPage - halfBoxSize);
            setLastPage(totalPages);
        } else if (currentPage + halfBoxSize > totalPages) {
            setFirstPage(totalPages - BUTTONS_TO_SHOW + 1);
            setLastPage(totalPages);
        } else {
            setFirstPage(currentPage - halfBoxSize);
            setLastPage(currentPage + halfBoxSize);
        }
    }

    public PageCounter getPageBox() {


        return this;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
}
