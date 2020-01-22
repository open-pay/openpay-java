package co.openpay.client.enums;

/**
 * The Enum UseCardPointsType.
 */
public enum UseCardPointsType {
    
    /** mixed. Cargo con pesos y puntos*/
    MIXED,
    
    /** none. Cargo en pesos*/
    NONE,
    
    /** only points. Cargo solo con puntos*/
    ONLY_POINTS;
    
    public static boolean isUseCardPoints(UseCardPointsType type) {
        return UseCardPointsType.MIXED.equals(type)||
               UseCardPointsType.ONLY_POINTS.equals(type);
    }

}