package fr.tiogars.data.system.serverinfo.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class JavaVersionInfo {

    @Schema(description = "Version Java utilisee par la JVM du serveur.", example = "25")
    private String javaVersion;

    @Schema(description = "Version runtime Java complete.", example = "25+36")
    private String runtimeVersion;

    @Schema(description = "Nom de la JVM.", example = "OpenJDK 64-Bit Server VM")
    private String vmName;

    @Schema(description = "Fournisseur de la JVM.", example = "Oracle Corporation")
    private String vmVendor;

    @Schema(description = "Nom du systeme d'exploitation hebergeant le serveur.", example = "Windows 11")
    private String osName;

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getRuntimeVersion() {
        return runtimeVersion;
    }

    public void setRuntimeVersion(String runtimeVersion) {
        this.runtimeVersion = runtimeVersion;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getVmVendor() {
        return vmVendor;
    }

    public void setVmVendor(String vmVendor) {
        this.vmVendor = vmVendor;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}
