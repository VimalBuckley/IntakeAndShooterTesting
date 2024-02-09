// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class Robot extends TimedRobot {
    private CommandJoystick stick;
    private Intake intake;
    private Shooter shooter;

    @Override
    public void robotInit() {
        stick = new CommandJoystick(1);
        intake = new Intake();
        shooter = new Shooter();

        ShuffleboardTab tab = Shuffleboard.getTab("Intake And Shooter Testing");
        tab.add(intake);
        tab.add(shooter);

        stick.button(12).onTrue(intake.zeroTilt());

        stick.button(2).onTrue(
            intake.setTilt(-35).andThen(
                intake.setOutput(0.3)
            )
        ).onFalse(
            intake.setOutput(0).andThen(
                intake.setTilt(5)
            ).andThen(
                shooter.pivot(-6.15).andThen(shooter.load(-0.25))
            ).andThen(
                Commands.waitSeconds(1.5)
            ).andThen(
                intake.setOutput(-0.25)
            ).andThen(
                shooter.waitTillNote().withTimeout(4)
            ).andThen(
                shooter.load(0).alongWith(intake.setOutput(0)).alongWith(intake.setTilt(-8.5))
            )
        );

        stick.button(1).onTrue(
            shooter.shoot(
                -4000, -4500
            ).andThen(
                Commands.waitSeconds(4)
            ).andThen(
                shooter.load(-1)
            )
        ).onFalse(
            shooter.off().andThen(shooter.load(0))
        );
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        intake.coastTilt().initialize();
    }
}
