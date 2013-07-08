package com.qifa;

import intradoc.common.ExecutionContext;
import intradoc.common.LocaleUtils;
import intradoc.common.ServiceException;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;

import intradoc.provider.Provider;
import intradoc.provider.Providers;

import intradoc.server.Service;
import intradoc.server.ServiceData;
import intradoc.server.ServiceManager;
import intradoc.server.UserStorage;

import intradoc.shared.UserData;

import java.util.Properties;

public class BaseServiceHelper {
    public void executeService(DataBinder binder, String userName,
                               boolean suppressServiceError) throws DataException,
                                                                    ServiceException {
        Workspace workspace = getSystemWorkspace();

        String cmd = binder.getLocal("IdcService");
        if (cmd == null) {
            throw new DataException("!csIdcServiceMissing");
        }
        ServiceData serviceData = ServiceManager.getFullService(cmd);
        if (serviceData == null) {
            throw new DataException(LocaleUtils.encodeMessage("!csNoServiceDefined",
                                                              null, cmd));
        }

        Service service =
            ServiceManager.createService(serviceData.m_classID, workspace,
                                         null, binder, serviceData);

        UserData fullUserData = getFullUserData(userName, service, workspace);
        service.setUserData(fullUserData);
        binder.m_environment.put("REMOTE_USER", userName);
        ServiceException error = null;
        try {
            service.setSendFlags(true, true);

            service.initDelegatedObjects();

            service.globalSecurityCheck();

            service.preActions();

            service.doActions();

            service.postActions();

            service.updateSubjectInformation(true);
            service.updateTopicInformation(binder);
        } catch (ServiceException e) {
            error = e;
        } finally {
            service.cleanUp(true);
            workspace.releaseConnection();
        }

        if (error != null)
            if (suppressServiceError) {
                error.printStackTrace();
                if (binder.getLocal("StatusCode") == null) {
                    binder.putLocal("StatusCode",
                                    String.valueOf(error.m_errorCode));

                    binder.putLocal("StatusMessage", error.getMessage());
                }
            } else {
                throw new ServiceException(error.m_errorCode,
                                           error.getMessage());
            }
    }

    public Workspace getSystemWorkspace() {
        Workspace workspace = null;
        Provider wsProvider = Providers.getProvider("SystemDatabase");
        if (wsProvider != null)
            workspace = (Workspace)wsProvider.getProvider();
        return workspace;
    }

    public UserData getFullUserData(String userName, ExecutionContext cxt,
                                    Workspace ws) throws DataException,
                                                         ServiceException {
        if (ws == null)
            ws = getSystemWorkspace();
        UserData userData =
            UserStorage.retrieveUserDatabaseProfileDataFull(userName, ws, null,
                                                            cxt, true, true);

        ws.releaseConnection();
        return userData;
    }
}
