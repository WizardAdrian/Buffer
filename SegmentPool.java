package com.jiuyan.infashion.lib.publish.story;

/**
 * Created by Adrian on 2016/4/16.
 * E-mail:aliu@in66.com
 */
public class SegmentPool {

    static final long MAX_SIZE = 65536L;
    static Segment next;
    static long byteCount;

    private SegmentPool() {
    }

    static Segment take() {
        Class var0 = SegmentPool.class;
        synchronized (SegmentPool.class) {
            if (next != null) {
                Segment result = next;
                next = result.next;
                result.next = null;
                byteCount -= 2048L;
                return result;
            }
        }
        return new Segment();
    }

    static void recycle(Segment segment) {
        if (segment.next == null && segment.prev == null) {
            if (!segment.shared) {
                Class var1 = SegmentPool.class;
                synchronized (SegmentPool.class) {
                    if (byteCount + 2048L <= 65536L) {
                        byteCount += 2048L;
                        segment.next = next;
                        next = segment;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    static class Segment {
        /**
         * True if other segments or byte strings use the same byte array.
         */
        boolean shared;
        /**
         * Next segment in a linked or circularly-linked list.
         */
        Segment next;

        /**
         * Previous segment in a circularly-linked list.
         */
        Segment prev;
    }
}
