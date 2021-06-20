package our.yurivongella.instagramclone.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class SliceHelper {
    public static <T> Slice<T> getSlice(List<T> content, Pageable pageable) {
        boolean hasNext = false;

        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    public static <T> boolean checkLast(final List<T> content, final int pageSize) {
        if (content.size() > pageSize) {
            return true;
        }
        return false;
    }

    public static <T> List<T> getContents(final List<T> content, final boolean hasNext, final int pageSize) {
        if (hasNext) {
            content.remove(pageSize);
        }
        return content;
    }
}
