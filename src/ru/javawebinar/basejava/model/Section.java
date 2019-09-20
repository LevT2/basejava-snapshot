package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.storage.serializer.Storable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Externalizable;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Section implements Serializable, Storable {
}
