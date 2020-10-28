package kg.megacom.discountservice.services.impl;

import kg.megacom.discountservice.dao.PlacePhoneRep;
import kg.megacom.discountservice.mappers.PlacePhoneMapper;
import kg.megacom.discountservice.models.dto.PlacePhoneDto;
import kg.megacom.discountservice.models.entity.Place;
import kg.megacom.discountservice.models.entity.PlacePhone;
import kg.megacom.discountservice.services.PlacePhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlacePhoneServiceImpl implements PlacePhoneService {

    @Autowired
    private PlacePhoneRep placePhoneRep;

    @Override
    public List<PlacePhoneDto> save(List<PlacePhoneDto> placePhoneDtoList, Place place) {
        List<PlacePhone> placePhones = PlacePhoneMapper.INSTANCE.toPlacePhoneList(placePhoneDtoList);
        placePhones = placePhones.stream().peek(x->x.setPlace(place)).collect(Collectors.toList());
        placePhones = placePhoneRep.saveAll(placePhones);
        return PlacePhoneMapper.INSTANCE.toPlacePhoneDtoList(placePhones);
    }

    @Override
    public List<PlacePhoneDto> updatePlacePhones(List<PlacePhoneDto> placePhoneDtoList, Place place) {
        List<PlacePhone> placePhones = placePhoneRep.findAllByPlace_Id(place.getId());
        placePhoneDtoList.removeIf(p->placePhones.contains(p.getPhone()));
        List<PlacePhone> newPlacePhoneList = placePhoneDtoList.stream().filter(p->!placePhones.contains(p.getPhone())).map(p->{
            PlacePhone placePhone = new PlacePhone();
            placePhone.setPhone(p.getPhone());
            placePhone.setPlace(place);
            return placePhone;
        }).collect(Collectors.toList());
         newPlacePhoneList.addAll(placePhones);
         newPlacePhoneList = placePhoneRep.saveAll(newPlacePhoneList);
        return PlacePhoneMapper.INSTANCE.toPlacePhoneDtoList(newPlacePhoneList);
    }

    @Override
    public List<PlacePhone> findAllByPlaceIds(List<Place> placeList) {
        List<PlacePhone> allPlacePhones = new ArrayList<>();
        for (Place p : placeList
             ) {
            List<PlacePhone> placePhones = placePhoneRep.findAllByPlace_Id(p.getId());
            allPlacePhones.addAll(placePhones);
        }
        return allPlacePhones;
    }
}