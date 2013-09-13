package org.utgenome.format.fasta;

import org.xerial.db.sql.BeanResultHandler;
import org.xerial.util.log.Logger;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author Taro L. Saito
 */
public abstract class SequenceRetrieverBase implements BeanResultHandler<FASTADatabase.NSeq> {

    private static Logger _logger = Logger.getLogger(SequenceRetrieverBase.class);

    private final int start;
    private final int end;
    private final boolean isReverseStrand;

    private static HashMap<Character, Character> reverseStrandTable = new HashMap<Character, Character>();

    static {
        reverseStrandTable.put('a', 't');
        reverseStrandTable.put('A', 'T');
        reverseStrandTable.put('g', 'c');
        reverseStrandTable.put('G', 'C');
        reverseStrandTable.put('t', 'a');
        reverseStrandTable.put('T', 'A');
        reverseStrandTable.put('c', 'g');
        reverseStrandTable.put('C', 'G');
    }

    public SequenceRetrieverBase(int start, int end, boolean isReverseStrand) {

        assert (start <= end);
        this.start = start;
        this.end = end;

        this.isReverseStrand = isReverseStrand;
    }

    public void handle(FASTADatabase.NSeq seq) throws SQLException {
        int rangeStart = ((seq.getStart() < start) ? start - seq.getStart() : 0);
        int rangeEnd = ((end > seq.getEnd()) ? seq.getLength() : end - seq.getStart() + 1);
        output(seq.getSubSequence(rangeStart, rangeEnd));
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isReverseStrand() {
        return isReverseStrand;
    }

    public abstract void output(String subSequence);

    public char getComplement(char base) {
        Character ch = reverseStrandTable.get(base);
        if (ch == null)
            return 'N';
        else
            return ch.charValue();
    }

    public void init() {

    }

    public void finish() {

    }

    public void handleException(Exception e) throws Exception {
        _logger.error(e);
    }

}

