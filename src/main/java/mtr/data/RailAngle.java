package mtr.data;

import net.minecraft.util.math.*;

public class RailAngle {

    public static final int ScaleFactor = 1024;
    public static final double PropertyDegree = 22.5;
    public static final int PropertyFactor = (int) (ScaleFactor * PropertyDegree);
    public static final int RightAngle = 90 * ScaleFactor;
    public static final int HalfCircleAngle = 180 * ScaleFactor;
    public static final int CircleAngle = 360 * ScaleFactor;

    public static final RailAngle NORTH = new RailAngle( - 90 * ScaleFactor );
    public static final RailAngle SOUTH = new RailAngle(   90 * ScaleFactor );
    public static final RailAngle EAST  = new RailAngle(    0 * ScaleFactor );
    public static final RailAngle WEST  = new RailAngle( -180 * ScaleFactor );
    public static final RailAngle NE    = new RailAngle( - 45 * ScaleFactor );
    public static final RailAngle NW    = new RailAngle( -135 * ScaleFactor );
    public static final RailAngle SE    = new RailAngle(   45 * ScaleFactor );
    public static final RailAngle SW    = new RailAngle(  135 * ScaleFactor );
    public static final RailAngle UP    = new RailAngle(    0 * ScaleFactor );
    public static final RailAngle DOWN  = new RailAngle(  180 * ScaleFactor );

    public static final Vec3d VNORTH = new Vec3d( 0, 0, -1 );
    public static final Vec3d VSOUTH = new Vec3d( 0, 0,  1 );
    public static final Vec3d VEAST  = new Vec3d( 1, 0,  0 );
    public static final Vec3d VWEST  = new Vec3d(-1, 0,  0 );
    public static final Vec3d VNE    = new Vec3d( 1, 0, -1 );
    public static final Vec3d VNW    = new Vec3d(-1, 0, -1 );
    public static final Vec3d VSE    = new Vec3d( 1, 0,  1 );
    public static final Vec3d VSW    = new Vec3d(-1, 0,  1 );

    public final int angle;
    public static final double ACCEPT_THRESHOLD = 1E-4;
    public static final int ACCEPT_ANGLE_ERROR = 10;

    public RailAngle(int angle) {
        this.angle = angle;
    }

    public static int PropertyToInternal(int property) {
        return property * PropertyFactor;
    }

    public RailAngle(Vec3i PosDiff) {
        if ((PosDiff.getZ() == 0) && (PosDiff.getX() == 0)) {
            this.angle = 0; // Warning : Default to north
        } else {
            this.angle = (int) (MathHelper.atan2(PosDiff.getZ(), PosDiff.getX()) * ScaleFactor / Math.PI);
        }
    }

    public static final RailAngle GetRailAngle(Direction Direction) {
        switch (Direction) {
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case EAST:
                return EAST;
            case WEST:
                return WEST;
            case UP:
                return UP;
            case DOWN:
                return DOWN;
            default:
                return NORTH; // Weird IDEA requirement
        }
    }

    public Direction RoundtoDirection(RailAngle angle) {
        if (angle == UP) return Direction.UP;
        if (angle == DOWN) return Direction.DOWN;
        int roundAngle = (Math.round( (float) NormalizeAngleInt(this.angle) / ( 90 * ScaleFactor ))) * ( 90 * ScaleFactor );
        switch (roundAngle) {
            case   0:                 return Direction.EAST;
            case   90 * ScaleFactor:  return Direction.SOUTH;
            case -180 * ScaleFactor:  return Direction.WEST;
            case - 90 * ScaleFactor:  return Direction.NORTH;
            default:
                return Direction.NORTH;
        }
    }
    public RailAngle GetRailAngleFromDegree(int degree) {
        return new RailAngle(degree * ScaleFactor);
    }

    public RailAngle getOpposite() { // Noted that UP.getOpposite != DOWN
        if (this.angle >= 0) {
            return new RailAngle(this.angle - HalfCircleAngle);
        } else {
            return new RailAngle(this.angle + HalfCircleAngle);
        }
    }


    // Except Inline
    public final double getDegreeD() {
        return (double) this.angle / ScaleFactor;
    }

    public final float getDegreeF() {
        return (float) this.angle / ScaleFactor;
    }

    public final double getRadD() {
        return (double) this.angle / ScaleFactor / 180 * Math.PI;
    }

    public final float getRadF() {
        return (float) getRadD();
    }

    private static int NormalizeAngleInt(int ScaledAngle) { // Private due to unsafe for > 360 degrees
        if (ScaledAngle >= HalfCircleAngle) {
            return (ScaledAngle - CircleAngle);
        } else if (ScaledAngle < (-HalfCircleAngle)) {
            return (ScaledAngle + CircleAngle);
        } else {
            return (ScaledAngle);
        }
    }

    public static int DegreeToProperty(float viewAngle) {
        int angle = ((int) Math.round( viewAngle / PropertyDegree) + ((int) (270 / PropertyDegree)));
        // We use standard Coordinate system, inverting and adding 90 degrees for rotating.
        return angle % (HalfCircleAngle / PropertyFactor);
    }

    public static int DegreeToInternal(double DegreeAngle) {
        int angle = ((int) Math.round( DegreeAngle * ScaleFactor) + ((int) (270 * ScaleFactor)));
        // We use standard Coordinate system, inverting and adding 90 degrees for rotating.
        return NormalizeAngleInt(angle % CircleAngle);
    }

    public RailAngle add(RailAngle angle2) {
        int sum = this.angle + angle2.angle;
        return new RailAngle(NormalizeAngleInt(sum));
    }

    public RailAngle sub(RailAngle angle2) {
        int sub = this.angle - angle2.angle;
        return new RailAngle(NormalizeAngleInt(sub));
    }

    public RailAngle div(double real) {
        return new RailAngle(NormalizeAngleInt((int) (this.angle / real)));
    }

    public RailAngle div(int real) {
        return new RailAngle(NormalizeAngleInt(this.angle / real));
    }

    public boolean isVertical(RailAngle angle2) {
        return RightAngle == Math.abs(this.angle - angle2.angle);
    }

    public boolean isVertical() {
        return RightAngle == Math.abs(this.angle);
    }

    public boolean isVertical(BlockPos Pos) {
        double degreeD = this.getDegreeD();
        return Math.abs(Pos.getX() * Math.cos(degreeD) + Pos.getZ() * Math.sin(degreeD)) < ACCEPT_THRESHOLD;
    }

    public boolean isParallel(RailAngle angle2) {
        return (this.angle == angle2.angle) || ( Math.abs(this.angle - angle2.angle) == HalfCircleAngle);
    }

    public boolean isParallel_Threshold(RailAngle angle2) {
        return (Math.abs(this.angle - angle2.angle) < ACCEPT_ANGLE_ERROR) || ( Math.abs(this.angle + HalfCircleAngle - angle2.angle) < ACCEPT_ANGLE_ERROR);
    }

    public boolean isParallel() {
        return (this.angle == 0) || ( Math.abs(this.angle) == HalfCircleAngle);
    }

    public boolean isParallel(double angle2) {
        return ((this.angle - angle2) < ACCEPT_THRESHOLD) || (( Math.abs(this.angle - angle2) - HalfCircleAngle) < ACCEPT_THRESHOLD);
    }

    public boolean isParallel(Vec3i vec3i) {
        return Math.abs(vec3i.getX() * this.cos() - vec3i.getZ() * this.sin()) < ACCEPT_THRESHOLD;
    }

    public boolean isSame(RailAngle angle2) {
        return this.angle == angle2.angle;
    }

    public boolean isSame_Threshold(RailAngle angle2) {
        return (Math.abs(this.angle - angle2.angle) < ACCEPT_ANGLE_ERROR);
    }

    public boolean isAcuteAngle(RailAngle angle2) {
        return RightAngle > Math.abs(this.angle - angle2.angle);
    }

    // is it less or equal a right angle?
    public boolean isLERightAngle(RailAngle angle2) {
        return RightAngle <= Math.abs(this.angle - angle2.angle);
    }

    public Vec3d toUnitVec3d() {
        switch (this.angle) {
            case   0:                 return VEAST;
            case   45 * ScaleFactor:  return VSE;
            case   90 * ScaleFactor:  return VSOUTH;
            case  135 * ScaleFactor:  return VSW;
            case -180 * ScaleFactor:  return VWEST;
            case -135 * ScaleFactor:  return VNW;
            case - 90 * ScaleFactor:  return VNORTH;
            case - 45 * ScaleFactor:  return VNE;
            default:
                return new Vec3d( Math.cos( this.getRadD() ) ,0 , Math.sin( this.getRadD() ) );
        }
    }

    final static double SQRT2_2 = Math.sqrt(2) / 2;
    public double sin(){
        switch (this.angle) {
            case   0:                 return  0;
            case   45 * ScaleFactor:  return  SQRT2_2;
            case   90 * ScaleFactor:  return  1;
            case  135 * ScaleFactor:  return  SQRT2_2;
            case -180 * ScaleFactor:  return  0;
            case -135 * ScaleFactor:  return -SQRT2_2;
            case - 90 * ScaleFactor:  return -1;
            case - 45 * ScaleFactor:  return -SQRT2_2;
            default:
                return Math.sin( this.getRadD() );
        }
    }

    public double cos(){
        switch (this.angle) {
            case   0:                 return  1;
            case   45 * ScaleFactor:  return  SQRT2_2;
            case   90 * ScaleFactor:  return  0;
            case  135 * ScaleFactor:  return -SQRT2_2;
            case -180 * ScaleFactor:  return -1;
            case -135 * ScaleFactor:  return -SQRT2_2;
            case - 90 * ScaleFactor:  return  0;
            case - 45 * ScaleFactor:  return  SQRT2_2;
            default:
                return Math.cos( this.getRadD() );
        }
    }

    public double tan(){
        switch (this.angle) {
            case   0:                 return  0;
            case   45 * ScaleFactor:  return  1;
            case   90 * ScaleFactor:  return  1E100;
            case  135 * ScaleFactor:  return -1;
            case -180 * ScaleFactor:  return  0;
            case -135 * ScaleFactor:  return  1;
            case - 90 * ScaleFactor:  return  1E100;
            case - 45 * ScaleFactor:  return -1;
            default:
                return Math.tan( this.getRadD() );
        }
    }
}
