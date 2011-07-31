package com.mebigfatguy.polycasso;

/**
 * a default implementation of a score for the error in a image against a target image
 */
public class DefaultScore implements Score {

    /**
     * a worst case score
     */
    public static final DefaultScore MAX_SCORE = new DefaultScore(Long.MAX_VALUE);
    
    long overallScore;
    
    /** 
     * constructs a score with the specified delta
     * @param delta the delta score
     */
    public DefaultScore(long delta) {
        overallScore = delta;
    }
    
    /**
     * returns the sum of the square of pixel errors
     * @return the delta between a generate image and the target
     */
    public long getDelta() {
        return overallScore;
    }
    
    /**
     * compares this core to another
     * 
     * @param o the score to compare to
     * @return whether the two scores have the same delta
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DefaultScore))
            return false;
        
        return getDelta() == ((DefaultScore) o).getDelta();
    }
    
    /**
     * generates a hash code for this score
     * 
     * @return the hash code of this score
     */
    @Override
    public int hashCode() {
        return (int) overallScore;
    }
    
    /**
     * returns a string representation of the score
     * 
     * @return the score as a string
     */
    @Override
    public String toString() {
        return String.valueOf(overallScore);
    }
}
