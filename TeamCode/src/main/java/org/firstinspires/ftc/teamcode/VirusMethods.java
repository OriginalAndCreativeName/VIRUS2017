package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public abstract class VirusMethods extends VirusHardware{
    public void runMotors(double Left0, double Left1, double Right0, double Right1, double steerMagnitude){
        if (Left0!=0&&Left1!=0&&Right0!=0&&Right1!=0) {
            steerMagnitude *= -2 * Math.max(Math.max(Left0, Left1), Math.max(Right0, Right1));
        }
        Left0=Left0-steerMagnitude;
        Left1=Left1-steerMagnitude;
        Right0=Right0+steerMagnitude;
        Right1=Right1+steerMagnitude;
        //make sure no exception thrown if power > 0
        Left0 = Range.clip(Left0, -maxPower, maxPower);
        Left1 = Range.clip(Left1, -maxPower, maxPower);
        Right0 = Range.clip(Right0, -maxPower, maxPower);
        Right1 = Range.clip(Right1, -maxPower, maxPower);
        rmotor0.setPower(Right0);
        rmotor1.setPower(Right1);
        lmotor0.setPower(Left0);
        lmotor1.setPower(Left1);
    }

    public void runMotors(double Left0, double Left1, double Right0, double Right1){
        //make sure no exception thrown if power > 0
        Left0 = Range.clip(Left0, -maxPower, maxPower);
        Left1 = Range.clip(Left1, -maxPower, maxPower);
        Right0 = Range.clip(Right0, -maxPower, maxPower);
        Right1 = Range.clip(Right1, -maxPower, maxPower);
        rmotor0.setPower(Right0);
        rmotor1.setPower(Right1);
        lmotor0.setPower(Left0);
        lmotor1.setPower(Left1);
    }

    public void setMotorPositions(int Left0, int Left1, int Right0, int Right1, double power){
        resetEncoder();
        lmotor0.setTargetPosition(Left0);
        lmotor1.setTargetPosition(Left1);
        rmotor0.setTargetPosition(Right0);
        rmotor1.setTargetPosition(Right1);
        runMotors(power,power,power,power);
        while (lmotor0.isBusy()||lmotor1.isBusy()||rmotor0.isBusy()||rmotor1.isBusy());
    }

    public boolean setMotorPositionsINCH(double Left0, double Left1, double Right0, double Right1, double power){
        resetEncoder();
        lmotor0.setTargetPosition((int)(Left0/inPerPulse));
        lmotor1.setTargetPosition((int)(Left1/inPerPulse));
        rmotor0.setTargetPosition((int)(Right0/inPerPulse));
        rmotor1.setTargetPosition((int)(Right1/inPerPulse));
        runMotors(power,power,power,power);
        if (!lmotor0.isBusy()&&!lmotor1.isBusy()&&!rmotor0.isBusy&&!rmotor1.isBusy){
            return true;
        }
        else{
            return false;
        }
    }

    public void resetEncoder(){
        runMotors(0,0,0,0);
        lmotor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lmotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rmotor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rmotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while (lmotor0.isBusy()||lmotor1.isBusy()||rmotor0.isBusy()||rmotor1.isBusy());
    }

    public void waitTime(int time){
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateControllerValues(){
        lefty = -gamepad1.left_stick_y;
        leftx = -gamepad1.left_stick_x;
        righty = -gamepad1.right_stick_y;
        rightx = -gamepad1.right_stick_x;
        rtrigger = -gamepad1.right_trigger;
        ltrigger = -gamepad1.left_trigger;
        double scalar = Math.max(Math.abs(lefty-leftx), Math.abs(lefty+leftx));
        double magnitude = Math.sqrt(lefty*lefty+leftx*leftx);
        var1= (lefty-leftx)*magnitude/scalar;
        var2= (lefty+leftx)*magnitude/scalar;
    }
}
