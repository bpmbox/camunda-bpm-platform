drop index ACT_IDX_HI_PRO_INST_END;
drop index ACT_IDX_HI_PRO_I_BUSKEY;
drop index ACT_IDX_HI_PRO_INST_TENANT_ID;
drop index ACT_IDX_HI_PRO_INST_PROC_DEF_KEY;
drop index ACT_IDX_HI_PRO_INST_PROC_TIME;
drop index ACT_IDX_HI_PI_PDEFID_END_TIME;
drop index ACT_IDX_HI_PRO_INST_ROOT_PI;
drop index ACT_IDX_HI_PRO_INST_RM_TIME;

drop index ACT_IDX_HI_ACTINST_ROOT_PI;
drop index ACT_IDX_HI_ACT_INST_START;
drop index ACT_IDX_HI_ACT_INST_END;
drop index ACT_IDX_HI_ACT_INST_PROCINST;
drop index ACT_IDX_HI_ACT_INST_COMP;
drop index ACT_IDX_HI_ACT_INST_STATS;
drop index ACT_IDX_HI_ACT_INST_TENANT_ID;
drop index ACT_IDX_HI_ACT_INST_PROC_DEF_KEY;
drop index ACT_IDX_HI_AI_PDEFID_END_TIME;

drop index ACT_IDX_HI_TASKINST_ROOT_PI;
drop index ACT_IDX_HI_TASK_INST_TENANT_ID;
drop index ACT_IDX_HI_TASK_INST_PROC_DEF_KEY;
drop index ACT_IDX_HI_TASKINST_PROCINST;
drop index ACT_IDX_HI_TASKINSTID_PROCINST;

drop index ACT_IDX_HI_DETAIL_ROOT_PI;
drop index ACT_IDX_HI_DETAIL_PROC_INST;
drop index ACT_IDX_HI_DETAIL_ACT_INST;
drop index ACT_IDX_HI_DETAIL_CASE_INST;
drop index ACT_IDX_HI_DETAIL_CASE_EXEC;
drop index ACT_IDX_HI_DETAIL_TIME;
drop index ACT_IDX_HI_DETAIL_NAME;
drop index ACT_IDX_HI_DETAIL_TASK_ID;
drop index ACT_IDX_HI_DETAIL_TENANT_ID;
drop index ACT_IDX_HI_DETAIL_PROC_DEF_KEY;
drop index ACT_IDX_HI_DETAIL_BYTEAR;

drop index ACT_IDX_HI_IDENT_LNK_USER;
drop index ACT_IDX_HI_IDENT_LNK_GROUP;
drop index ACT_IDX_HI_IDENT_LNK_TENANT_ID;
drop index ACT_IDX_HI_IDENT_LNK_PROC_DEF_KEY;
drop index ACT_IDX_HI_IDENT_LINK_TASK;

drop index ACT_IDX_HI_VARINST_ROOT_PI;
drop index ACT_IDX_HI_PROCVAR_PROC_INST;
drop index ACT_IDX_HI_PROCVAR_NAME_TYPE;
drop index ACT_IDX_HI_CASEVAR_CASE_INST;
drop index ACT_IDX_HI_VAR_INST_TENANT_ID;
drop index ACT_IDX_HI_VAR_INST_PROC_DEF_KEY;
drop index ACT_IDX_HI_VARINST_BYTEAR;

drop index ACT_IDX_HI_INCIDENT_TENANT_ID;
drop index ACT_IDX_HI_INCIDENT_PROC_DEF_KEY;
drop index ACT_IDX_HI_INCIDENT_PROCINST;

drop index ACT_IDX_HI_JOB_LOG_PROCINST;
drop index ACT_IDX_HI_JOB_LOG_PROCDEF;
drop index ACT_IDX_HI_JOB_LOG_TENANT_ID;
drop index ACT_IDX_HI_JOB_LOG_JOB_DEF_ID;
drop index ACT_IDX_HI_JOB_LOG_PROC_DEF_KEY;
drop index ACT_IDX_HI_JOB_LOG_EX_STACK;

drop index ACT_HI_EXT_TASK_LOG_PROCINST;
drop index ACT_HI_EXT_TASK_LOG_PROCDEF;
drop index ACT_HI_EXT_TASK_LOG_PROC_DEF_KEY;
drop index ACT_HI_EXT_TASK_LOG_TENANT_ID;
drop index ACT_IDX_HI_EXTTASKLOG_ERRORDET;

drop index ACT_IDX_HI_OP_LOG_PROCINST;
drop index ACT_IDX_HI_OP_LOG_PROCDEF;
drop index ACT_IDX_HI_OP_LOG_TASK;

drop index ACT_IDX_HI_ATTACHMENT_CONTENT;
drop index ACT_IDX_HI_ATTACHMENT_PROCINST;
drop index ACT_IDX_HI_ATTACHMENT_TASK;

drop index ACT_IDX_HI_COMMENT_TASK;
drop index ACT_IDX_HI_COMMENT_PROCINST;

drop table ACT_HI_PROCINST;
drop table ACT_HI_ACTINST;
drop table ACT_HI_VARINST;
drop table ACT_HI_TASKINST;
drop table ACT_HI_DETAIL;
drop table ACT_HI_COMMENT;
drop table ACT_HI_ATTACHMENT;
drop table ACT_HI_OP_LOG;
drop table ACT_HI_INCIDENT;
drop table ACT_HI_JOB_LOG;
drop table ACT_HI_BATCH;
drop table ACT_HI_IDENTITYLINK;
drop table ACT_HI_EXT_TASK_LOG;