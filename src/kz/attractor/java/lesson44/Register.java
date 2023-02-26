package kz.attractor.java.lesson44;

public class Register {
    private boolean start;
    private boolean state;
    private String regMess;

    public Register(boolean startReg ,boolean state) {
        start = startReg;
        this.state = state;
        regMess = returnMess();
    }
    public String returnMess(){
        if(state){
            return "Удачная регистрация";
        }else
            return "регистрация не удалась";
    }
    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getRegMess() {
        return regMess;
    }

    public void setRegMess(String regMess) {
        this.regMess = regMess;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
