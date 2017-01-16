/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author User
 */
public class DocRoute {
    StringBuffer title = new StringBuffer();
    
    StringBuffer query = new StringBuffer();
    
    StringBuffer routeId = new StringBuffer("");
    StringBuffer orgId = new StringBuffer("");
    Calendar rdate = Calendar.getInstance();
    
    StringBuffer carId = new StringBuffer("");
        StringBuffer gsmId = new StringBuffer("");
        StringBuffer typeId = new StringBuffer("");
        StringBuffer rashod = new StringBuffer("");
        StringBuffer rashodSpec = new StringBuffer("");
        StringBuffer climb = new StringBuffer("");
        StringBuffer volume = new StringBuffer("");
        
    StringBuffer driverId = new StringBuffer("");
        StringBuffer docNumber = new StringBuffer("");
        StringBuffer docSeries = new StringBuffer("");
        Calendar docDateEnd = Calendar.getInstance();
        StringBuffer driverPhone = new StringBuffer("");
    
    StringBuffer sId1 = new StringBuffer("");        
    StringBuffer sId2 = new StringBuffer("");
    
    Calendar dateStart = Calendar.getInstance();
    Calendar dateEnd = Calendar.getInstance();
    
    StringBuffer directionId = new StringBuffer("");
    
    StringBuffer timeStart = new StringBuffer("");
    StringBuffer timeEnd = new StringBuffer("");
    
    StringBuffer odoStart = new StringBuffer("");
    StringBuffer odoEnd = new StringBuffer("");
    
    StringBuffer timefStart = new StringBuffer("");
    StringBuffer timefEnd = new StringBuffer("");
    
    StringBuffer gsmGet = new StringBuffer("");
    StringBuffer gsmStart = new StringBuffer("");
    StringBuffer gsmEnd = new StringBuffer("");
    
    StringBuffer specStart = new StringBuffer("");
    StringBuffer specEnd = new StringBuffer("");
    
    StringBuffer gruz = new StringBuffer("ТКО");
    StringBuffer ezdki = new StringBuffer("");
    StringBuffer dist = new StringBuffer("");
  
    StringBuffer shift = new StringBuffer("");    
    StringBuffer comment = new StringBuffer("");
    
    StringBuffer manager = new StringBuffer("");
    
    boolean saved = false;
    boolean closed = false;
    boolean deleted = false;
    boolean modified = false;
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");    
    SimpleDateFormat sdf = new SimpleDateFormat("YYY-MM-dd");
    
    void setDefaultValues(){
        routeId.setLength(0);
        orgId.setLength(0);
        rdate.setTime(new Date());
        
        dateStart.clear();
        dateEnd.clear();
        
        carId.setLength(0);
        gsmId.setLength(0);
        typeId.setLength(0);
        rashod.setLength(0);
        rashodSpec.setLength(0);
        climb.setLength(0);
        volume.setLength(0);
        
        driverId.setLength(0);
        docNumber.setLength(0);
        docSeries.setLength(0);
        docDateEnd.clear();
        driverPhone.setLength(0);
        
        sId1.setLength(0);
        sId2.setLength(0);
        
        dateStart.clear();
        dateEnd.clear();
        
        directionId.setLength(0);
        
        timeStart.setLength(0);
        timeEnd.setLength(0);
        
        odoStart.setLength(0);
        odoEnd.setLength(0);
        
        timefStart.setLength(0);
        timefEnd.setLength(0);
        
        gsmGet.setLength(0);
        gsmStart.setLength(0);
        gsmEnd.setLength(0);
        
        specStart.setLength(0);
        specEnd.setLength(0);

        gruz.setLength(0);
            gruz.append("ТКО");        
        ezdki.setLength(0);
        dist.setLength(0);
        
        shift.setLength(0);
        comment.setLength(0);
        
        manager.setLength(0);
        
        saved = false;
        closed = false;
        deleted = false;
        modified = false;
    }
    // *************************************************************************
    // ********************** SETTERS ******************************************
    // *************************************************************************
    
    void setStatus(String id){
        switch (id){
            case "0":{saved = true; closed = false; deleted = false; break;}
            case "1":{saved = true; closed = true; deleted = false; break;}
            case "3":{saved = true; closed = false; deleted = true; break;}
        }
        modified = false;
    }
    void setSaved(boolean saved){
        this.saved = saved;
    }
    void setClosed(boolean closed){
        this.closed = closed;
    }
    void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    void setModified(boolean modified) {
        this.modified = modified;
    }
   // **************************************************************************
    void setRouteId(String id){
        routeId.setLength(0);
        routeId.append(id);
    }
    void setOrg(String id){
        orgId.setLength(0);
        orgId.append(id);
    }
    void setDate(Calendar date){                             
        if (date == null)
            rdate.clear();
        else
            rdate.setTime(date.getTime());
    }
    // *************************************************************************
    void setCarID(String id) {
        carId.setLength(0);
        carId.append(id);        
    }   
    void setGsmId(String id) {
        gsmId.setLength(0);
        gsmId.append(id);
    }    
    void setTypeId(String id) {
        typeId.setLength(0);
        typeId.append(id);
    }    
    void setRashod(String str) {
        rashod.setLength(0);
        rashod.append(str);
    }
    void setRashodSpec(String str) {
        rashodSpec.setLength(0);
        rashodSpec.append(str);
    }
    void setClimb(String str) {
        climb.setLength(0);
        climb.append(str);
    }   
    void setVolume(String str){
        volume.setLength(0);
        volume.append(str);
    }  
    // *************************************************************************    
    void setDriverId(String id) {
        driverId.setLength(0);
        driverId.append(id);        
    }
    void setDocNumber(String str) {
        docNumber.setLength(0);
        docNumber.append(str);
    }
    void setDocSeries(String str) {
        docSeries.setLength(0);
        docSeries.append(str);
    }
    void setDocDateEnd(String str) {
        // установить дату документа
    }
    void setDriverPhone(String str) {
        driverPhone.setLength(0);
        driverPhone.append(str);
    }
    // *************************************************************************
    void setSopr1(String id) {
        sId1.setLength(0);
        sId1.append(id);
    }
    void setSopr2(String id) {
        sId2.setLength(0);
        sId2.append(id);
    }
    // *************************************************************************
    void setDateStart(Calendar date) {
        if (date == null)
            dateStart.clear();
        else
            dateStart.setTime(date.getTime()); 
    }
    void setDateEnd(Calendar date) {
        if (date == null)
            dateEnd.clear();
        else
            dateEnd.setTime(date.getTime());
    }
    // *************************************************************************
    void setDirection(String id) {
        directionId.setLength(0);
        directionId.append(id);
    }    
    // *************************************************************************
    void setTimeStart(String time) {
        timeStart.setLength(0);
        timeStart.append(time);
    }
    void setTimeEnd(String time) {
        timeEnd.setLength(0);
        timeEnd.append(time);
    }
    // *************************************************************************
    void setOdoStart(String str) {
        odoStart.setLength(0);
        odoStart.append(str);
    }
    void setOdoEnd(String str) {
        odoEnd.setLength(0);
        odoEnd.append(str);
    }
    // *************************************************************************
    void setTimefStart(String time) {
        timefStart.setLength(0);
        timefStart.append(time);
    }
    void setTimefEnd(String time) {
        timefEnd.setLength(0);
        timefEnd.append(time);
    }    
    // *************************************************************************
    void setGSMGet(String str) {
        gsmGet.setLength(0);
        gsmGet.append(str);
    }
    void setGSMStart(String str) {
        gsmStart.setLength(0);
        gsmStart.append(str);
    }
    void setGSMEnd(String str) {
        gsmEnd.setLength(0);
        gsmEnd.append(str);
    }
    // *************************************************************************    
    void setSpecStart(String str) {
        specStart.setLength(0);
        specStart.append(str);
    }
    void setSpecEnd(String str) {
        specEnd.setLength(0);
        specEnd.append(str);
    }
    // *************************************************************************
    void setGruz(String str) {
        gruz.setLength(0);
        gruz.append(str);
    }
    void setEzdki(String str) {
        ezdki.setLength(0);
        ezdki.append(str);
    }
    void setDist(String str) {
        dist.setLength(0);
        dist.append(str);
    }
    // *************************************************************************
    void setShift(String str) {
        shift.setLength(0);
        shift.append(str);
    }
    // *************************************************************************
    void setComment(String str) {
        comment.setLength(0);
        comment.append(str);
    }
    // *************************************************************************
    void setManagerId(String str) {
        manager.setLength(0);
        manager.append(str);
    }
    // *************************************************************************
    // ********************* GETTERS *******************************************
    // *************************************************************************    
    // -------------------------------------------------------------------------
    String getRouteId() {
        return routeId.toString();
    }
    Calendar getRDate() {
        if (rdate.get(Calendar.YEAR) != 1970)
            return rdate;
        else
            return null;
    }
    String getOrg() {
        return orgId.toString();
    }
    // -------------------------------------------------------------------------
    String getCarID() {
        return carId.toString();
    }    
    String getGsmId() {
        return gsmId.toString();
    }
    String getTypeId() {
        return typeId.toString();
    }
    String getRashod() {
        return rashod.toString();
    }
    String getRashodSpec() {
        return rashodSpec.toString();
    }
    String getClimb() {
        return climb.toString();
    }
    String getVolume() {
        return volume.toString();
    }  
    // -------------------------------------------------------------------------
    String getDriverId() {
        return driverId.toString();
    }
    String getDocNumber() {
        return docNumber.toString();
    }
    String getDocSeries() {
        return docSeries.toString();
    }
    Calendar getDocDateEnd() {
        return docDateEnd;
    }
    String getDriverPhone() {
        return driverPhone.toString();
    }
    // -------------------------------------------------------------------------
    String getSopr1() {
        return sId1.toString();
    }
    String getSopr2(){
        return sId2.toString();
    }      
    // -------------------------------------------------------------------------
    Calendar getDateStart() {
        if (dateStart.get(Calendar.YEAR) != 1970)
            return dateStart;
        else
            return null;
    }
    Calendar getDateEnd() {
        if (dateEnd.get(Calendar.YEAR) != 1970)
            return dateEnd;
        else
            return null;
    }
    // -------------------------------------------------------------------------
    String getDirection() {
        return directionId.toString();
    }
    // -------------------------------------------------------------------------
    String getTimeStart() {
        return timeStart.toString();
    }
    String getTimeEnd() {
        return timeEnd.toString();
    }
    // -------------------------------------------------------------------------
    String getOdoStart() {
        return odoStart.toString();
    }
    String getOdoEnd() {
        return odoEnd.toString();        
    }
    // -------------------------------------------------------------------------
    String getTimefStart() {
        return timefStart.toString();
    }
    String getTimefEnd() {
        return timefEnd.toString();
    }
    // -------------------------------------------------------------------------
    String getGSMGet() {
        return gsmGet.toString();
    }
    String getGsmStart() {
        return gsmStart.toString();
    }
    String getGsmEnd() {
        return gsmEnd.toString();
    }
    // -------------------------------------------------------------------------
    String getSpecStart() {
        return specStart.toString();
    }
    String getSpecEnd() {
        return specEnd.toString();
    }
    // -------------------------------------------------------------------------
    String getGruz() {
        return gruz.toString();
    }
    String getEzdki() {
        return ezdki.toString();
    }
    String getDist() {
        return dist.toString();
    }
    // -------------------------------------------------------------------------
    String getShift(){
        return shift.toString();
    }
    // -------------------------------------------------------------------------
    String getComment(){
        return comment.toString();
    }
    // -------------------------------------------------------------------------
    String getManagerId() {
        return manager.toString();
    }
    
    String getTitle() {
        title.setLength(0);
        title.append("Документ 'Путевой Лист'");
        if (this.getRouteId().equals(""))
            title.append(" №-(нет номера)");
        else
            title.append(" №-"+this.getRouteId());
        if (this.isClosed())
            title.append(" [Заблокирован]");
        if (this.isModified())
            title.append(" [*]");
        return title.toString();
    }
    // *************************************************************************
    // *************************************************************************
    // *************************************************************************
    void rDateClear() {
        rdate.clear();
    }    
    void dateStartClear() {
        dateStart.clear();
    }    
    void dateEndClear(){
        dateEnd.clear();
    }    
    
    boolean isSaved() {
        return saved;
    }
    boolean isClosed() {
        return closed;
    }
    boolean isDeleted() {
        return deleted;
    }  
    boolean isModified() {
        return modified;
    }
    String toNull(String s) {
        if (s.equals(""))
            return "NULL";
        else
            return s;
    }
    String toZero(String s) {
        if (s.equals(""))
            return "0";
        else
            return s.replace(",", ".");
    }
    String dateF(Calendar date){
        
        //System.out.println(date.getTime());
        //System.out.println(sdf.format(date.getTime()));
        //System.out.println(date.get(Calendar.YEAR)+"-"+date.get(Calendar.MONTH)+"-"+date.get(Calendar.DAY_OF_MONTH));
        int yy=date.get(Calendar.YEAR);
        int mm=date.get(Calendar.MONTH)+1;
        int dd=date.get(Calendar.DAY_OF_MONTH);
        
        if (date != null)
            return "date('"+yy+"-"+mm+"-"+dd+"')";
        else
            return null;
    }    
    
    StringBuffer save(String manager){   
        query.setLength(0);
            query.append("INSERT INTO route (date, organization_id, car_id, driver_id, date_start, odo_start, gsm_start, gsm_get, ");
            query.append("date_end, odo_end, gsm_end, manager_id, coment, special_start, special_end, soprovod_1, soprovod_2, volume_route, direction_id, ");
            query.append("ezdki, distance_route, gruz, fact_time_start, fact_time_end, time_start, time_end, shift) VALUES (")
                .append(dateF(getRDate())).append(", ")
                .append(orgId).append(", ")
                .append(toNull(carId.toString())).append(", ")
                .append(toNull(driverId.toString())).append(", ")
                .append(dateF(getDateStart())).append(", ")
                .append(toZero(odoStart.toString())).append(", ")
                .append(toZero(gsmStart.toString())).append(", ")
                .append(toZero(gsmGet.toString())).append(", ")
                .append(dateF(getDateEnd())).append(", ")
                .append(toZero(odoEnd.toString())).append(", ")
                .append(toZero(gsmEnd.toString())).append(", ")
                .append(manager).append(", '")
                .append(comment).append("', ")
                .append(toZero(specStart.toString())).append(", ")
                .append(toZero(specEnd.toString())).append(", ")
                .append(toNull(sId1.toString())).append(", ")
                .append(toNull(sId2.toString())).append(", ")
                .append(toZero(volume.toString())).append(", ")
                .append(toNull(directionId.toString())).append(", ")
                .append(toZero(ezdki.toString())).append(", ")
                .append(toZero(dist.toString())).append(", '")
                .append(gruz).append("', '")
                .append(timefStart).append("', '")
                .append(timefEnd).append("', '")
                .append(timeStart).append("', '")
                .append(timeEnd).append("', ")
                .append(toNull(shift.toString())).append(")");            
        return query;
    }
    StringBuffer update(String manager){
        query.setLength(0);
            query.append("UPDATE route ")
                .append("SET date=").append(dateF(getRDate()))
                .append(", organization_id=").append(orgId)
                .append(", car_id=").append(toNull(carId.toString()))
                .append(", driver_id=").append(toNull(driverId.toString()))
                .append(", date_start=").append(dateF(getDateStart()))
                .append(", odo_start=").append(toZero(odoStart.toString()))
                .append(", gsm_start=").append(toZero(gsmStart.toString()))
                .append(", gsm_get=").append(toZero(gsmGet.toString()))
                .append(", date_end=").append(dateF(getDateEnd()))
                .append(", odo_end=").append(toZero(odoEnd.toString()))
                .append(", gsm_end=").append(toZero(gsmEnd.toString()))
                .append(", manager_id=").append(manager)
                .append(", coment='").append(comment)
                .append("', special_start=").append(toZero(specStart.toString()))
                .append(", special_end=").append(toZero(specEnd.toString()))
                .append(", soprovod_1=").append(toNull(sId1.toString()))
                .append(", soprovod_2=").append(toNull(sId2.toString()))
                .append(", volume_route=").append(toZero(volume.toString()))
                .append(", direction_id=").append(toNull(directionId.toString()))
                .append(", ezdki=").append(toZero(ezdki.toString()))
                .append(", distance_route=").append(toZero(dist.toString()))
                .append(", gruz='").append(gruz)
                .append("', fact_time_start='").append(timefStart)
                .append("', fact_time_end='").append(timefEnd)
                .append("', time_start='").append(timeStart)
                .append("', time_end='").append(timeEnd).append("',")
                .append(" shift=").append(toNull(shift.toString()))
                .append(" WHERE id=").append(routeId);                        
            return query;
    }
    String close(){
        return "UPDATE route SET status=1 WHERE id="+routeId;
    }
}
