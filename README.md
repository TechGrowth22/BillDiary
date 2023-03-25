# BillDiary


## To run and build the project
    we need to follow the following steps intellij 
    Go to File -> Settings -> Build, Execution, Deployment -> Build Tools -> Maven -> Runner -> Check the option 'Delegate IDE builds/ run actions to maven'

### Bugs resolution
    ** For cyclic dependeny between Invoice and InvoiceItems we need to remove @Data annotations from one of the 
    entity ** 
