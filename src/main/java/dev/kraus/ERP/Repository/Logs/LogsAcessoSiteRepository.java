package dev.kraus.ERP.Repository.Logs;


import dev.kraus.ERP.Model.Logs.LogsAcessoSite.LogsAcessoSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsAcessoSiteRepository extends JpaRepository<LogsAcessoSite, Long> {

    List<LogsAcessoSite> findTop100ByOrderByAcessadoEmDesc();
}
