package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Shooter extends SubsystemBase {
    private CANSparkMax tiltMotor;
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private CANSparkMax loaderMotor;
    private AnalogInput lidar;
    public Shooter() {
        rightMotor = new CANSparkMax(13, MotorType.kBrushless);
        leftMotor = new CANSparkMax(14, MotorType.kBrushless);
        tiltMotor = new CANSparkMax(15, MotorType.kBrushless);
        loaderMotor = new CANSparkMax(16, MotorType.kBrushless);
        lidar = new AnalogInput(1);

        rightMotor.getPIDController().setP(0.3);
        leftMotor.getPIDController().setP(0.3);
        tiltMotor.getPIDController().setP(0.5);
        tiltMotor.getPIDController().setOutputRange(-0.3, 0.3);
        tiltMotor.setSoftLimit(SoftLimitDirection.kForward, 28);
        tiltMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
    }

    public boolean hasNote() {
        return lidar.getValue() < 2200;
    }

    public Command pivot(double angle) {
        return Commands.runOnce(
            () -> tiltMotor.getPIDController().setReference(angle, ControlType.kPosition),
            this
        );
    }

    public Command load(double output) {
        return Commands.runOnce(
            () -> loaderMotor.set(output), this
        );
    }

    public Command waitTillNote() {
        return Commands.waitUntil(() -> hasNote());
    }

    public Command shoot(double left, double right) {
        return Commands.runOnce(
            () -> {
                leftMotor.getPIDController().setReference(left, ControlType.kVelocity);
                rightMotor.getPIDController().setReference(right, ControlType.kVelocity);
            }
        );
    }
}
