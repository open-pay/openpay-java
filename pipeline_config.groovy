/**
  Estas configuraciones son especificas para este repositorio/proyecto
*/
/** Configuraciones generales */
jdk_tool = 'Java-OP11'

jte {
    /** pipeline template a usar */
    pipeline_template = "jar_library"
}
/** Librerias que se usaran */
libraries{
    maven
}
pull_request_enabled = false
git_credentials_id = "github_openpay"
